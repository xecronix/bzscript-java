package com.techport80.bzscript;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import com.techport80.bzscript.Language.Bz.Kind;

class ParserTests {
    @Test void parsesNestedFunctionWithNoArguments() throws Exception {
        AstToken root = parse("printnum(inputn());");
        assertEquals(1, root.tokens.size());
        AstToken outer = root.tokens.getFirst();
        assertEquals(Kind.LANG_FUN, outer.kind);
        assertEquals("printnum", outer.name);
        assertEquals(1, outer.tokens.size());
        AstToken inner = outer.tokens.getFirst();
        assertEquals(Kind.LANG_FUN, inner.kind);
        assertEquals("inputn", inner.name);
        assertTrue(inner.tokens.isEmpty());
    }

    private AstToken parse(String source) throws Exception {
        return Parser.parse(new Lexer().getTokens(source, "parser-test.bzs"));
    }

    private void rejects(String source, String diagnostic) {
        Exception error = assertThrows(Exception.class, () -> parse(source));
        assertTrue(error.getMessage().contains(diagnostic), error::getMessage);
    }

    @Test void createsRootBlockAndIgnoresEmptyStatements() throws Exception {
        AstToken root = parse(";;;;");
        assertEquals(Kind.BLOCK_OPEN, root.kind);
        assertTrue(root.tokens.isEmpty());
    }

    @Test void separatesStatementsWithoutCreatingEmptyStatements() throws Exception {
        AstToken root = parse("1;;2;3;;");
        assertEquals(3, root.tokens.size());
    }

    @Test void parsesFinalStatementWithoutSemicolon() throws Exception {
        // Whitespace finishes the lexer's word without terminating the statement.
        AstToken root = parse("1\n");
        assertEquals(1, root.tokens.size());
        assertEquals(Kind.LITERAL_NUMBER, root.tokens.getFirst().kind);
        assertEquals("1", root.tokens.getFirst().name);
    }

    @Test void parsesGroupContainingSingleNumber() throws Exception {
        AstToken root = parse("(1);");
        assertEquals(Kind.BLOCK_OPEN, root.kind);
        assertEquals(1, root.tokens.size());
        AstToken group = root.tokens.getFirst();
        assertEquals(Kind.GROUP_OPEN, group.kind);
        assertEquals(1, group.tokens.size());
        AstToken number = group.tokens.getFirst();
        assertEquals(Kind.LITERAL_NUMBER, number.kind);
        assertEquals("1", number.name);
        assertTrue(number.tokens.isEmpty());
    }

    @Test void returnsExpressionTreeWithMultiplicationBeforeAddition() throws Exception {
        AstToken sum = parse("1 + 2 * 3;").tokens.getFirst();
        assertEquals(Kind.ADDITION, sum.kind);
        assertEquals(2, sum.tokens.size());
        assertEquals("1", sum.tokens.getFirst().name);
        AstToken product = sum.tokens.get(1);
        assertEquals(Kind.MULTIPLY, product.kind);
        assertEquals(List.of("2", "3"), product.tokens.stream().map(t -> t.name).toList());
    }

    @Test void rejectsMissingBinaryOperands() {
        rejects("* 1;", "Expected token before [*] on line [1] column [1]");
        rejects("1 +;", "Expected token after [+] on line [1] column [3]");
    }

    @Test void rejectsEmptyGroupsUsedAsBinaryOperands() {
        rejects("() + 1;", "Invalid empty group on line [1] column [1]");
        rejects("1 + ();", "Invalid empty group on line [1] column [5]");
    }

    @Test void rejectsGroupsWithMultipleValuesUsedAsBinaryOperands() {
        rejects("(1, 2) + 3;", "Unexpected group reduction on line [1] column [1]");
        rejects("1 + (2, 3);", "Unexpected group reduction on line [1] column [5]");
    }

    @Test void rejectsUnclosedGroupsAndBlocks() {
        rejects("(1;", "Reached end of BzScript before closing group.");
        rejects("loop {1;", "Reached end of BzScript prematurely.");
    }

    @Test void rejectsLetWithoutAssignment() {
        rejects("let #x;", "Expected assignment after [let]");
    }

    @Test void rejectsMissingOrEmptyIfCondition() {
        rejects("if;", "Expected condition after [if]");
        rejects("if ();", "Found empty group after [if]");
    }

    @Test void rejectsNullAndEmptyTokenLists() {
        assertThrows(Exception.class, () -> Parser.parse(null));
        assertThrows(Exception.class, () -> Parser.parse(List.of()));
    }
}
