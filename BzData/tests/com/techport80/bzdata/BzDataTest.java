package com.techport80.bzdata;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

class BzDataTest {

    // Uses the public API already shared, without requiring record getters.
    // Strip only document-edge whitespace: field/comment whitespace is inside
    // the pipes and terminating semicolon and remains part of the comparison.
    private String loadAndSave(String source) throws Exception {
        try (StringReader reader = new StringReader(source);
             StringWriter writer = new StringWriter()) {
            BzData data = new BzData(reader, writer);
            data.open();
            data.save();
            return writer.toString().strip();
        }
    }

    @Test
    void semicolonImmediatelyAfterOpeningPipeIsDataAndCannotCloseField() throws Exception {
        assertThrows(Exception.class, () -> loadAndSave("|;"));
        assertEquals("|;|;", loadAndSave("|;|;"));
    }

    @Test
    void rejectsZeroFieldFirstRecordsEvenWhenValidDataFollows() {
        for (String source : new String[] {";", "!;", ";|a|;", "!;|a|;"}) {
            Exception error = assertThrows(Exception.class, () -> loadAndSave(source), source);
            assertFalse(error instanceof NullPointerException,
                "Empty first records must produce a validation error: " + source);
        }
    }

    @Test
    void headerLookupTrimsNamesWhileSavingPreservesOriginalWhitespace() throws Exception {
        String source = "!| Name | first column|\tAge\t| second column;\n|Ada||36|;";
        try (StringReader reader = new StringReader(source);
             StringWriter writer = new StringWriter()) {
            BzData data = new BzData(reader, writer);
            data.open();
            assertEquals(0, data.getHeaderIndex("Name"));
            assertEquals(0, data.getHeaderIndex(" \tName\t "));
            assertEquals(1, data.getHeaderIndex("Age"));
            assertEquals(-1, data.getHeaderIndex("name"));
            data.save();
            assertEquals(source + "\n", writer.toString());
        }
    }

    @Test
    void rejectsDuplicateAndEmptyHeaderNamesAfterTrimming() {
        assertAll(
            () -> assertThrows(Exception.class, () -> loadAndSave("!|Name|| Name |;")),
            () -> assertThrows(Exception.class, () -> loadAndSave("!| \t |;"))
        );
    }

    @Test
    void headerMarkerAllowsOnlyWhitespaceBeforeFirstField() throws Exception {
        assertEquals("!|name|;", loadAndSave("! \t\r\n |name|;"));
        assertAll(
            () -> assertThrows(Exception.class, () -> loadAndSave("!!|name|;")),
            () -> assertThrows(Exception.class, () -> loadAndSave("! \t!|name|;")),
            () -> assertThrows(Exception.class, () -> loadAndSave("! text|name|;"))
        );
    }

    @Test
    void fieldWriterEscapesDataButLeavesCommentAlone() {
        BzField field = new BzField("C:\\temp|file", " comment\\text !");

        assertEquals("|C:\\\\temp\\|file| comment\\text !", field.toString());
        assertEquals("C:\\temp|file", field.getData(), "Writing must not mutate data");
        assertEquals(" comment\\text !", field.getComment());
    }

    @Test
    void emptyAndWhitespaceOnlyInputsProduceEmptyOutput() throws Exception {
        assertEquals("", loadAndSave(""));
        assertEquals("", loadAndSave(" \t\r\n "));
    }

    @Test
    void readsThreeFieldsWithTheirFollowingComments() throws Exception {
        String source = "|first|Comment 1|second|Comment 2|third|Comment 3;";

        assertEquals(source, loadAndSave(source));
    }

    @Test
    void preservesEmptyFieldsAndWhitespaceInsideFieldsAndComments() throws Exception {
        String record = "|| \t| first\nsecond | comment\n |third| trailing  ;";

        assertEquals(record, loadAndSave(" \r\n" + record + "\r\n\t"));
    }

    @Test
    void semicolonsAndExclamationMarksInsideDataAreLiteral() throws Exception {
        String source = "|even ;more ;data!||!not a comment|;";

        assertEquals(source, loadAndSave(source));
    }

    @Test
    void recognizedEscapesRoundTripIncludingBackslashFollowedByPipe() throws Exception {
        String slash = "\\";
        String source = "|more" + slash + "| data|"
                      + "|C:" + slash + slash + "temp" + slash + slash + "|"
                      + "|" + slash + slash + slash + "||;";

        assertEquals(source, loadAndSave(source));
        assertEquals("|\\\\\\||", new BzField("\\|").toString());
    }

    @Test
    void unknownEscapesRemainLiteralAndAreCanonicalizedWhenSaved() throws Exception {
        // Input contains literal backslash+n and backslash+s, not newlines.
        String source = "|\\n and \\s|" + " comment ends in \\" + "|next|;";
        String expected = "|\\\\n and \\\\s|" + " comment ends in \\" + "|next|;";

        // The backslash in the COMMENT must not consume the next opening pipe.
        assertEquals(expected, loadAndSave(source));
    }

    @Test
    void headerNamesAreCaseSensitiveAndOrdinaryCommentsSkipFieldCounts() throws Exception {
        String header = "!|Name| first column|name| second column;";
        String first = "|same||one|;";
        String comment = "! literal | pipes \\ and !\n remain comment text ;";
        String second = "|same||two|;"; // Duplicate first-column values are allowed.

        String saved = loadAndSave(header + "\n" + first + "\n" + comment + "\n" + second);
        int h = saved.indexOf(header);
        int a = saved.indexOf(first);
        int c = saved.indexOf(comment);
        int b = saved.indexOf(second);
        
        //System.out.println(saved.toString());
        
        assertTrue(h >= 0, "Header and its field comments should survive");
        assertTrue(a > h, "First data record should follow the header");
        assertTrue(c > a, "Ordinary comment should survive in its original position");
        assertTrue(b > c, "Final data record should follow the comment");
    }

    @Test
    void rejectsFieldCountMismatchesWithAndWithoutHeader() {
        assertAll(
            () -> assertThrows(Exception.class,
                () -> loadAndSave("|a||b|;|c|;")),
            () -> assertThrows(Exception.class,
                () -> loadAndSave("!|one||two|;|a|;")),
            () -> assertThrows(Exception.class,
                () -> loadAndSave("!|one|;|a||b|;"))
        );
    }

    @Test
    void rejectsInvalidHeadersMissingTerminatorsAndUnexpectedLeadingText() {
        assertAll(
            () -> assertThrows(Exception.class,
                () -> loadAndSave("!|name||name|;"), "Duplicate header names"),
            () -> assertThrows(Exception.class,
                () -> loadAndSave("!||;"), "Empty header name"),
            () -> assertThrows(Exception.class,
                () -> loadAndSave("|value|"), "Missing record terminator"),
            () -> assertThrows(Exception.class,
                () -> loadAndSave("|unclosed;"), "Semicolon cannot close a field"),
            () -> assertThrows(Exception.class,
                () -> loadAndSave("|value|;!unterminated comment"), "Missing comment terminator"),
            () -> assertThrows(Exception.class,
                () -> loadAndSave("unexpected |value|;"), "Text before first field")
        );
    }
}
