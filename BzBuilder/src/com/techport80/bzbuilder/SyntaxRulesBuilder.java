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

public class SyntaxRulesBuilder {

    private BzData data;

    private final String templatePath;
    private final String dataPath;
    private final String outputPath;

    private final List<String> headers = new ArrayList<>();

    private static final List<String> TABLE_NAMES = List.of(
        "RejectAnyLeft",
        "RejectAnyRight",
        "AllowOnlyLeft",
        "AllowOnlyRight"
    );

    public SyntaxRulesBuilder(
            String templatePath,
            String dataPath,
            String outputPath) {

        this.templatePath = templatePath;
        this.dataPath = dataPath;
        this.outputPath = outputPath;
    }

    public void build() throws Exception {
        System.out.println("Syntax Checker Builder:");

        try (Reader reader = new FileReader(
                Path.of(dataPath, "syntax_rules.bzdata").toFile())) {

            data = new BzData(reader);
            data.open();
        }

        BzRecord header = data.getCursor().current();

        if (header == null || header.getKind() != BzRecord.Kind.HEADER) {
            throw new Exception("Syntax rules require a header record.");
        }

        headers.clear();

        for (int i = 0; i < data.getMaxFieldCount(); i++) {
            headers.add(header.getField(i).getData());
        }

        String template = Files.readString(
            Path.of(templatePath, "SyntaxRules.tpl"));

        JEagle eagle = new JEagle(template);
        eagle.addAction("rules", new RulesAction());

        String content = eagle.parse(null);

        Files.writeString(
            Path.of(outputPath, "SyntaxRules.java"),
            content);
    }

    /*
     * Creates a substitution record using the existing BzData API.
     * All temporary context construction stays in this one method.
     */
    private BzRecord context(
            String ruleTableName,
            String subject,
            String kind) throws Exception {

        BzData contextData = new BzData(null);

        BzRecord header =
            new BzRecord(contextData, BzRecord.Kind.HEADER);

        header.addField(new BzField("ruleTableName"));
        header.addField(new BzField("subject"));
        header.addField(new BzField("kind"));
        contextData.addRecord(header);

        BzRecord row =
            new BzRecord(contextData, BzRecord.Kind.DATA);

        row.addField(new BzField(ruleTableName));
        row.addField(new BzField(subject));
        row.addField(new BzField(kind));
        contextData.addRecord(row);

        return row;
    }

    // Expands {@rules;} once for each of the four rule tables.
    private class RulesAction implements IActionRunner {

        @Override
        public String run(JEagle eagle, BzRecord unused)
                throws Exception {

            StringBuilder result = new StringBuilder();

            for (String section : TABLE_NAMES) {
                String mapName =
                    Character.toLowerCase(section.charAt(0))
                    + section.substring(1);

                eagle.addAction(
                    "ruleTable",
                    new RuleTableAction(section, mapName));

                eagle.rewind();
                result.append(
                    eagle.parse(context(mapName, "", "")));
            }

            return result.toString();
        }
    }

    // Expands {@ruleTable;} once for each data row in its section.
    private class RuleTableAction implements IActionRunner {

        private final String section;
        private final String mapName;

        RuleTableAction(String section, String mapName) {
            this.section = section;
            this.mapName = mapName;
        }

        @Override
        public String run(JEagle eagle, BzRecord unused)
                throws Exception {

            StringBuilder result = new StringBuilder();
            Cursor<BzRecord> cursor = data.getCursor();
            boolean foundTable = false;

            while (true) {
                BzRecord rec = cursor.current();

                if (rec.getKind() == BzRecord.Kind.COMMENT) {
                    if (foundTable) {
                        break;
                    }

                    String comment =
                        rec.getField(0).getComment().trim();

                    foundTable = comment.equals(section);
                }
                else if (foundTable
                        && rec.getKind() == BzRecord.Kind.DATA) {

                    String subject =
                        rec.getField("subject").getData().trim();

                    eagle.addAction(
                        "listOfKinds",
                        new ListOfKindsAction(rec, mapName, subject));

                    eagle.rewind();
                    result.append(
                        eagle.parse(context(mapName, subject, "")));
                }

                if (!cursor.next()) {
                    break;
                }
            }

            if (!foundTable) {
                throw new Exception(
                    "Could not find [%s] in BzData file."
                        .formatted(section));
            }

            return result.toString();
        }
    }

    // Expands {@listOfKinds;} once for each selected header.
    private class ListOfKindsAction implements IActionRunner {

        private final BzRecord source;
        private final String mapName;
        private final String subject;

        ListOfKindsAction(
                BzRecord source,
                String mapName,
                String subject) {

            this.source = source;
            this.mapName = mapName;
            this.subject = subject;
        }

        @Override
        public String run(JEagle eagle, BzRecord unused)
                throws Exception {

            StringBuilder result = new StringBuilder();

            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);

                if (header.equals("subject")) {
                    continue;
                }

                String value =
                    source.getField(i).getData().trim();

                if (value.equals("Y")) {
                    eagle.rewind();
                    result.append(
                        eagle.parse(context(mapName, subject, header)));
                }
            }

            return result.toString();
        }
    }
}