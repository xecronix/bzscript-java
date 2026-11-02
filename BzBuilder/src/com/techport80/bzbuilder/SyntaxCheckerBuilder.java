package com.techport80.bzbuilder;

import com.techport80.jeagle.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import com.techport80.bzdata.*;
import com.techport80.jlistcursor.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class SyntaxCheckerBuilder {
	
	private BzData data;
	private String templatePath;
	private String dataPath;
	private String outputPath;
	
	public SyntaxCheckerBuilder(String templatePath, String dataPath, String outputPath) {
		System.out.println("Syntax Checker Builder:");
		this.templatePath = templatePath;
		this.dataPath = dataPath;
		this.outputPath = outputPath;
	}
	
	private String readTemplate(String filePathToTemplate) throws Exception{
		 Path filePath = Path.of(filePathToTemplate);
		 String content = Files.readString(filePath);
		 return content;
	}
	
	public void build() throws Exception
	{
        try(
    			Reader reader = new FileReader(dataPath + "/syntax_rules.bzdata");
    			
            ){
                // Use data here.
            	BzData data = new BzData(reader);
            	data.open();
            	this.data = data;
            }
        
    	JEagle eagle = new JEagle(readTemplate(templatePath + "/SyntaxChecker.tpl"));
    	eagle.addAction("wordmapping", new WordMappingAction(data));
    	
    	String fileContent = eagle.parse(null);
    	Files.writeString(Path.of(outputPath +"/SyntaxChecker.java"), fileContent);    	
	}
	
	public class WordMappingAction implements IActionRunner{
		BzData data;
		public WordMappingAction(BzData data) {
			this.data = data;
		}
		@Override
		public String run(JEagle eagle, BzRecord rec) throws Exception {
			String retval = "";
			Cursor<BzRecord> db = data.getCursor();
			boolean foundKeywords = false;
			while(true)
			{
				rec = db.current();
				if (rec.getKind() == BzRecord.Kind.DATA && foundKeywords){
					eagle.rewind();
					retval += eagle.parse(rec);
				}
				else if (rec.getKind() == BzRecord.Kind.COMMENT){
						BzField f = rec.getField(0);
						if (f.getComment().trim().compareTo("Keywords") == 0) {
							retval += "\n//" + f.getComment();
							foundKeywords = true;	
						}
						else if (foundKeywords){
							retval += "\n//" + f.getComment();
						}
				}
				if (!db.next()) {break;}
			}
			
			if (!foundKeywords) {
				throw new Exception("Could not find [Keywords] in BzData file.");
			}
			return retval;
		}
	}
}
