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

public class AntStubBuilder {

    private BzData data;

    private final String templatePath;
    private final String dataPath;
    private final String outputPath;

    private final List<String> headers = new ArrayList<>();

    public AntStubBuilder(
            String templatePath,
            String dataPath,
            String outputPath) {

        this.templatePath = templatePath;
        this.dataPath = dataPath;
        this.outputPath = outputPath;
    }

    public void build() throws Exception {
        System.out.println("Ant Stub Builder:");
        
        List<String> stubClassNames = new ArrayList<String>(); 

        try (Reader reader = new FileReader(
                Path.of(dataPath, "language.bzdata").toFile())) {

            data = new BzData(reader);
            data.open();
        }

        BzRecord header = data.getCursor().current();

        if (header == null || header.getKind() != BzRecord.Kind.HEADER) {
            throw new Exception("Ant Stub Builder requires a header record.");
        }

        headers.clear();

        for (int i = 0; i < data.getMaxFieldCount(); i++) {
            headers.add(header.getField(i).getData());
        }
        
        Cursor<BzRecord> db = data.getCursor();
        db.next(); //skip the header.
        while (true) {
        	BzRecord rec = db.current();
        	if (rec.getKind() == BzRecord.Kind.DATA) {
        		String cName =rec.getField("eval_handler").getData().trim(); 
        		if (cName.length() > 0) {
        			Path p = Path.of(outputPath, cName + ".java");
        			if (!Files.exists(p)) {
        				stubClassNames.add(cName);
        				String template = Files.readString(
        			            Path.of(templatePath, "AntStub.tpl"));
       			        JEagle eagle = new JEagle(template);
       			        String content = eagle.parse(rec);
       			        Files.writeString(
       			            Path.of(outputPath,  cName + ".java"),
       			            content);
        			}
        		} 
        	}
        	if (!db.next()) {break;}
        }
    }
    
}
    
    
    

