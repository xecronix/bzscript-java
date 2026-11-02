package com.techport80.bzbuilder;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.techport80.bzdata.*;
import com.techport80.jeagle.*;
import com.techport80.jlistcursor.*;

public class EvaluatorBuilder {

    private BzData data;

    private final String templatePath;
    private final String dataPath;
    private final String outputPath;

    private final List<String> headers = new ArrayList<>();

    public EvaluatorBuilder(
            String templatePath,
            String dataPath,
            String outputPath) {

        this.templatePath = templatePath;
        this.dataPath = dataPath;
        this.outputPath = outputPath;
    }

    public void build() throws Exception {
        System.out.println("Evaluator Builder:");

        try (Reader reader = new FileReader(
                Path.of(dataPath, "language.bzdata").toFile())) {

            data = new BzData(reader);
            data.open();
        }

        BzRecord header = data.getCursor().current();

        if (header == null || header.getKind() != BzRecord.Kind.HEADER) {
            throw new Exception("Evaluator Builder requires a header record.");
        }

        headers.clear();

        for (int i = 0; i < data.getMaxFieldCount(); i++) {
            headers.add(header.getField(i).getData());
        }

        String template = Files.readString(
            Path.of(templatePath, "Evaluator.tpl"));

        JEagle eagle = new JEagle(template);
        eagle.addAction("antBzLanguage", new AntBzLanguage(data));

        String content = eagle.parse(null);

        Files.writeString(
            Path.of(outputPath, "Evaluator.java"),
            content);
    }
    
    
    
	public class AntBzLanguage implements IActionRunner{
		BzData data;
		public AntBzLanguage(BzData data) {
			this.data = data;
		}
		@Override
		public String run(JEagle eagle, BzRecord rec) throws Exception {
			String retval = "";
			Cursor<BzRecord> db = data.getCursor();
			while(true)
			{
				eagle.rewind();
				rec = db.current();
				if (rec.getKind() == BzRecord.Kind.COMMENT){

					if (rec.getField(0).getComment().trim().compareTo("Functions") == 0) {
						break;
					}
				}
				else if (rec.getKind() == BzRecord.Kind.DATA){
					if (rec.getField("eval_handler").getData().trim().length() > 0) {
						retval += eagle.parse(rec);
					}
				}
				
				if (!db.next()) {break;}
			}
			return retval;
		}
		
	}
    
    
    
}