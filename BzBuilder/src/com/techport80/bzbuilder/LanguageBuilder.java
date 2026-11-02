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

public class LanguageBuilder {
	
	private BzData data;
	private String templatePath;
	private String dataPath;
	private String outputPath;
	
	public LanguageBuilder(String templatePath, String dataPath, String outputPath) {
		System.out.println("Language Builder:");
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
    			Reader reader = new FileReader(dataPath + "/language.bzdata");
    			
            ){
                // Use data here.
            	BzData data = new BzData(reader);
            	data.open();
            	this.data = data;
            }
        
    	JEagle eagle = new JEagle(readTemplate(templatePath + "/language.tpl"));
    	
    	ConstAction constAction = new ConstAction(data);
    	eagle.addAction("const", constAction);
    	eagle.addAction("bzenum", new BzEnumAction(data));
    	eagle.addAction("symmapping", new SymMappingAction(data));
    	eagle.addAction("wordmapping", new WordMappingAction(data));
    	String fileContent = eagle.parse(null);
    	Files.writeString(Path.of(outputPath +"/Language.java"), fileContent);    	
	}
	
	
	public class SpellingAction implements ISubstitutionRunner{

		@Override
		public String run(String str) throws Exception {
			    return str.replace("\\", "\\\\")
			                .replace("\"", "\\\"")
			                .replace("\n", "\\n")
			                .replace("\r", "\\r")
			                .replace("\t", "\\t")
			                .replace("\b", "\\b")
			                .replace("\f", "\\f");
		}
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
	
	public class SymMappingAction implements IActionRunner{
		BzData data;
		public SymMappingAction(BzData data) {
			this.data = data;
		}
		@Override
		public String run(JEagle eagle, BzRecord rec) throws Exception {
			String retval = "";
			Cursor<BzRecord> db = data.getCursor();
			boolean symbolsFound = false;
			while(true)
			{
				eagle.rewind();
				rec = db.current();
				if (rec.getKind() == BzRecord.Kind.DATA){
					retval += eagle.parse(rec);
				}
				else if (rec.getKind() == BzRecord.Kind.COMMENT){
						BzField f = rec.getField(0);
						if (f.getComment().trim().compareTo("Keywords") == 0) {
							break;
						}
						else if (f.getComment().trim().compareTo("Symbols") == 0 && !symbolsFound) {
							retval += "\n//" + f.getComment();
							symbolsFound = true;
						}
						else if (symbolsFound){
							retval += "\n//" + f.getComment();
						}
						
				}
				if (!db.next()) {break;}
			}
			
			if (!symbolsFound) {
				throw new Exception("Could not find [Symbols] in BzData file.");
			}
			return retval;
		}
	}
	
	public class BzEnumAction implements IActionRunner{
		BzData data;
		public BzEnumAction(BzData data) {
			this.data = data;
		}
		@Override
		public String run(JEagle eagle, BzRecord rec) throws Exception {
			String retval = "";
			Cursor<BzRecord> db = data.getCursor();
			Set<String> enums = new HashSet<>();
			while(true)
			{
				eagle.rewind();
				rec = db.current();
				if (rec.getKind() == BzRecord.Kind.DATA){
					BzField f = rec.getField("kind");
					if(enums.add(f.getData())) {
						retval += eagle.parse(rec);
					}
				}
				if (!db.next()) {break;}
			}
			return retval;
		}
	}
	
	public class ConstAction implements IActionRunner{
		BzData data;
		public ConstAction(BzData data) {
			this.data = data;
		}
		@Override
		public String run(JEagle eagle, BzRecord rec) throws Exception {
			String retval = "";
			Cursor<BzRecord> db = data.getCursor();
			eagle.addSubstitution("spelling", new SpellingAction());
			while(true)
			{
				eagle.rewind();
				rec = db.current();
				if (rec.getKind() == BzRecord.Kind.COMMENT){
					// we'll do nothing for now.  I'd like to add comments if I figure out 
					// how to make them indent correctly
				}
				else if (rec.getKind() == BzRecord.Kind.DATA){
					retval += eagle.parse(rec);
				}
				
				if (!db.next()) {break;}
			}
			return retval;
		}
		
	}
}
