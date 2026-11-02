package com.techport80.jeagle;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import org.junit.jupiter.api.Test;
import com.techport80.bzdata.*;

class JEagleTests {
    @Test
    void substitutionRunnerReceivesFieldValueAndReplacesItsOutput() throws Exception {
        JEagle eagle = new JEagle("Hello {=name:}!");
        eagle.addSubstitution("name", value -> {
            assertEquals("Ada", value);
            return value.toUpperCase(java.util.Locale.ROOT);
        });
        assertEquals("Hello ADA!", eagle.parse(person("Ada")));
    }

    // Build a real table with headers: named lookup intentionally needs its parent.
    private BzRecord person(String name) throws Exception {
        BzData table = new BzData(new StringReader(""));
        BzRecord headers = new BzRecord(table, BzRecord.Kind.HEADER);
        headers.addField(new BzField("name"));
        table.addRecord(headers);
        BzRecord row = new BzRecord(table, BzRecord.Kind.DATA);
        row.addField(new BzField(name));
        table.addRecord(row);
        return row;
    }

    private String render(JEagle body, BzRecord context) throws Exception {
        return body.parse(context);
    }

    @Test
    void plainTextKeepsSpacesTabsAndNewlines() throws Exception {
        String text = "  Hello\tworld!\r\nNext line.\n";
        assertEquals(text, new JEagle(text).parse(person("Ada")));
    }

    @Test
    void substitutionInASentenceUsesTheNamedField() throws Exception {
        assertEquals("Hello Ada!", new JEagle("Hello {=name:}!").parse(person("Ada")));
    }

    @Test
    void adjacentSubstitutionsDoNotLoseCharacters() throws Exception {
        assertEquals("AdaAda", new JEagle("{=name:}{=name:}").parse(person("Ada")));
    }

    @Test
    void actionCanIgnoreItsBodyAndGenerateText() throws Exception {
        JEagle eagle = new JEagle("Before {@fixed;ignored:} after.");
        eagle.addAction("fixed", (body, context) -> "READY");
        assertEquals("Before READY after.", eagle.parse(person("Ada")));
    }

    @Test
    void actionBodyPreservesWhitespaceImmediatelyAfterSemicolon() throws Exception {
        JEagle eagle = new JEagle("[{@copy; \n\tHello {=name:}!  :}]");
        eagle.addAction("copy", this::render);
        assertEquals("[ \n\tHello Ada!  ]", eagle.parse(person("Ada")));
    }

    @Test
    void emptyActionBodyPassesNullAndStillRunsTheCallback() throws Exception {
        JEagle eagle = new JEagle("[{@empty;:}]");
        eagle.addAction("empty", (body, context) -> {
            assertNull(body);
            return "EMPTY";
        });
        assertEquals("[EMPTY]", eagle.parse(person("Ada")));
    }

    @Test
    void nestedActionRendersWhenRegisteredOnTheChild() throws Exception {
        JEagle eagle = new JEagle("{@outer;A{@inner;B{=name:}:}C:}D");
        eagle.addAction("outer", (body, context) -> {
            body.addAction("inner", this::render);
            return render(body, context);
        });
        assertEquals("ABAdaCD", eagle.parse(person("Ada")));
    }

    @Test
    void actionCanRenderItsBodyWithAnotherRecord() throws Exception {
        BzRecord grace = person("Grace");
        JEagle eagle = new JEagle("{=name:}/{@guest;{=name:}:}/{=name:}");
        eagle.addAction("guest", (body, context) -> render(body, grace));
        assertEquals("Ada/Grace/Ada", eagle.parse(person("Ada")));
    }

    @Test
    void loopingActionRewindsBodyBetweenPeople() throws Exception {
        BzRecord ada = person("Ada");
        BzRecord grace = person("Grace");
        JEagle eagle = new JEagle("People:{@people; [{=name:}]:}.");
        eagle.addAction("people", (body, context) -> {
            String first = render(body, ada);
            body.rewind();
            return first + render(body, grace);
        });
        assertEquals("People: [Ada] [Grace].", eagle.parse(ada));
    }

    @Test
    void rewindLetsTheWholeTemplateRenderWithNewContext() throws Exception {
        JEagle eagle = new JEagle("Hello {=name:}!");
        assertEquals("Hello Ada!", eagle.parse(person("Ada")));
        eagle.rewind();
        assertEquals("Hello Grace!", eagle.parse(person("Grace")));
    }

    @Test
    void missingFieldThrowsInsteadOfPreservingTheTag() throws Exception {
        JEagle eagle = new JEagle("{=country:}");
        BzRecord context = person("Ada");
        Exception error = assertThrows(Exception.class, () -> eagle.parse(context));
        assertTrue(error.getMessage().contains("country"));
    }

    @Test
    void unknownActionReportsItsNameAndOpeningPosition() throws Exception {
        JEagle eagle = new JEagle("Hi\n  {@missing;body:}");
        BzRecord context = person("Ada");
        Exception error = assertThrows(Exception.class, () -> eagle.parse(context));
        assertEquals("Unknown action [missing] on line [2] column [3]", error.getMessage());
    }

    @Test
    void actionNameRequiresASemicolonBeforeItsBody() {
        Exception error = assertThrows(Exception.class, () -> new JEagle("{@copy Hello:}"));
        assertEquals("Expected ; but found [ ] on line [1] column [7]", error.getMessage());
    }
}
