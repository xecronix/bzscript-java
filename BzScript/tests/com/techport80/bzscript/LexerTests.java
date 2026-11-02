package com.techport80.bzscript;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.techport80.bzscript.Language.Bz.Kind;
import com.techport80.jlistcursor.Cursor;

class LexerTests {
    private final Lexer lexer = new Lexer();
    private static final String FILE = "test.bzs";

    private List<AstToken> atoms(String source) {
        List<AstToken> tokens = new ArrayList<>();
        lexer.Atomize(tokens, source, FILE);
        return tokens;
    }

    private List<AstToken> symbols(String source) throws Exception {
        List<AstToken> tokens = new ArrayList<>();
        lexer.doSymbols(new Cursor<>(atoms(source)), tokens);
        return tokens;
    }

    private List<AstToken> strings(String source) throws Exception {
        List<AstToken> tokens = new ArrayList<>();
        lexer.doStringAndComment(new Cursor<>(symbols(source)), tokens);
        return tokens;
    }

    private void assertKinds(List<AstToken> tokens, Kind... expected) {
        assertEquals(List.of(expected), tokens.stream().map(t -> t.kind).toList());
    }

    @Test
    void atomizeEmptySourceProducesNoTokens() {
        assertTrue(atoms("").isEmpty());
    }

    @Test
    void atomizePreservesCharactersAndFileNames() {
        List<AstToken> tokens = atoms("ab;");
        assertEquals(List.of("a", "b", ";"), tokens.stream().map(t -> t.name).toList());
        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(FILE, tokens.get(i).fileName);
            assertEquals(1, tokens.get(i).lineNumber);
            assertEquals(i + 1, tokens.get(i).columnNumber);
            assertEquals(Kind.UNKNOWN, tokens.get(i).kind);
        }
    }

    @Test
    void atomizeTracksWhitespaceAndNewlinePositions() {
        List<AstToken> tokens = atoms(" \t\r\nx");
        assertKinds(tokens, Kind.WHITE_SPACE, Kind.WHITE_SPACE, Kind.WHITE_SPACE, Kind.EOL, Kind.UNKNOWN);
        assertEquals(1, tokens.get(3).lineNumber);
        assertEquals(4, tokens.get(3).columnNumber);
        assertEquals(2, tokens.get(4).lineNumber);
        assertEquals(1, tokens.get(4).columnNumber);
    }

    @Test
    void symbolsRecognizeSingleCharacterOperatorsAndPunctuation() throws Exception {
        assertKinds(symbols(";,#$(){}+-*/^=><`"), Kind.STATEMENT_END, Kind.LIST_SEPARATOR,
                Kind.VAR_NUMBER, Kind.VAR_STRING, Kind.GROUP_OPEN, Kind.GROUP_CLOSE,
                Kind.BLOCK_OPEN, Kind.BLOCK_CLOSE, Kind.ADDITION, Kind.SUBTRACTION,
                Kind.MULTIPLY, Kind.DIVIDE, Kind.EXPONENT, Kind.ASSIGN,
                Kind.COMPARE_GREATER, Kind.COMPARE_LESS, Kind.STRING_DELIMITER);
    }

    @Test
    void symbolsPreferTwoCharacterMatchesAndKeepStartPositions() throws Exception {
        List<AstToken> tokens = symbols("==>=<=!=//");
        assertKinds(tokens, Kind.COMPARE_EQ, Kind.COMPARE_GREATER_EQ,
                Kind.COMPARE_LESS_EQ, Kind.COMPARE_NOT_EQ, Kind.COMMENT);
        assertEquals(List.of("==", ">=", "<=", "!=", "//"),
                tokens.stream().map(t -> t.name).toList());
        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(i * 2 + 1, tokens.get(i).columnNumber);
        }
    }

    @Test
    void stringsPreserveWhitespaceAndCommentMarkers() throws Exception {
        List<AstToken> tokens = strings("`hello // world\n\t `");
        assertKinds(tokens, Kind.LITERAL_STRING);
        assertEquals("hello // world\n\t ", tokens.get(0).name);
        assertEquals(FILE, tokens.get(0).fileName);
        assertEquals(1, tokens.get(0).lineNumber);
        assertEquals(1, tokens.get(0).columnNumber);
    }

    @Test
    void stringsAllowEmptyLiteral() throws Exception {
        List<AstToken> tokens = strings("``");
        assertKinds(tokens, Kind.LITERAL_STRING);
        assertEquals("", tokens.get(0).name);
    }

    @Test
    void stringsDecodeDoubledBackticks() throws Exception {
        List<AstToken> tokens = strings("`a``b`");
        assertKinds(tokens, Kind.LITERAL_STRING);
        assertEquals("a`b", tokens.get(0).name);
    }

    @Test
    void stringsRejectUnclosedLiteralWithOpeningPosition() {
        Exception error = assertThrows(Exception.class, () -> strings("\n  `unfinished"));
        assertEquals("Did not find closing ` for string starting on line[2] column[3]", error.getMessage());
    }

    @Test
    void commentsEndAtNewlineOrEndOfSource() throws Exception {
        List<AstToken> tokens = strings("// ignored `text\n+// trailing");
        assertKinds(tokens, Kind.EOL, Kind.ADDITION);
        assertEquals(2, tokens.get(1).lineNumber);
        assertEquals(1, tokens.get(1).columnNumber);
    }

    @Test
    void compressRemovesWhitespaceButKeepsLiteralContents() throws Exception {
        List<AstToken> tokens = new ArrayList<>();
        lexer.doCompress(new Cursor<>(strings(" \t` a b `\r\n; ")), tokens);
        assertKinds(tokens, Kind.LITERAL_STRING, Kind.STATEMENT_END);
        assertEquals(" a b ", tokens.get(0).name);
    }

    @Test
    void unaryRejectsSignsAtStartOfInput() {
        for (String source : List.of("-1;", "+1;")) {
            Exception error = assertThrows(Exception.class,
                    () -> lexer.doUnary(new Cursor<>(symbols(source)), new ArrayList<>()));
            assertEquals("Unexpected [%s] at start of input on line [1] column [1]"
                    .formatted(source.substring(0, 1)), error.getMessage());
        }
    }

    @Test
    void unaryClassifiesGroupedSignsWhilePreservingBinarySigns() throws Exception {
        List<AstToken> result = lexer.getTokens("(-1)+(+2);", FILE);
        assertKinds(result, Kind.GROUP_OPEN, Kind.UNARY_NEGATIVE,
                Kind.LITERAL_NUMBER, Kind.GROUP_CLOSE, Kind.ADDITION,
                Kind.GROUP_OPEN, Kind.UNARY_POSITIVE, Kind.LITERAL_NUMBER,
                Kind.GROUP_CLOSE, Kind.STATEMENT_END);
    }

    @Test
    void unaryClassifiesConsecutiveSigns() throws Exception {
        List<AstToken> result = new ArrayList<>();
        // Classification only; the syntax checker rejects the missing operand.
        lexer.doUnary(new Cursor<>(symbols("(++)")), result);
        assertKinds(result, Kind.GROUP_OPEN, Kind.UNARY_POSITIVE,
                Kind.UNARY_POSITIVE, Kind.GROUP_CLOSE);
    }

    @Test
    void getTokensRecognizesKeywordAndStatementTerminator() throws Exception {
        List<AstToken> tokens = lexer.getTokens("break;", FILE);
        assertKinds(tokens, Kind.KW_BREAK, Kind.STATEMENT_END);
        assertEquals("break", tokens.get(0).name);
    }

    @Test
    void getTokensRejectsNumberWithoutStatementTerminator() {
        Exception error = assertThrows(Exception.class, () -> lexer.getTokens("42", FILE));
        assertEquals("Found End of file while attempting to identify [42] line [1] column [1]",
                error.getMessage());
    }
    
    @Test
    void getTokensRejectsReservedSymbolOutsideStringsAndComments() {
        Exception error = assertThrows(Exception.class,
            () -> lexer.getTokens("\n  @;", FILE));

        assertEquals(
            "Reserved [@] is not allowed on line [2] column [3]",
            error.getMessage());
    }

    @Test
    void getTokensRejectsVariablesWithoutNames() {
        for (String prefix : List.of("#", "$")) {
            Exception error = assertThrows(Exception.class,
                () -> lexer.getTokens("\n  " + prefix + ";", FILE),
                "Expected rejection for bare " + prefix);

            assertEquals(
                "Variables require a name [2] column [3]",
                error.getMessage());
        }
    }
    
    @Test
    void getTokensAllowsReservedSymbolsInsideStrings() throws Exception {
        String contents = "!\"%&':?@[]\\|~";
        List<AstToken> tokens = lexer.getTokens("`" + contents + "`;", FILE);

        assertKinds(tokens, Kind.LITERAL_STRING, Kind.STATEMENT_END);
        assertEquals(contents, tokens.get(0).name);
    }
}
