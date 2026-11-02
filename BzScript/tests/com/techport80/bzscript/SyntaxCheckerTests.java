package com.techport80.bzscript;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import com.techport80.bzscript.Language.Bz.Kind;

class SyntaxCheckerTests {
    private SyntaxChecker checker(String source) throws Exception {
        return new SyntaxChecker(new Lexer().getTokens(source, "syntax-test.bzs"));
    }

    private void accepts(String source) throws Exception {
        SyntaxChecker checker = checker(source);
        assertTrue(checker.check(), () -> source + ": " + checker.getErrors());
        assertTrue(checker.getErrors().isEmpty());
    }

    private void rejects(String source, String diagnostic) throws Exception {
        SyntaxChecker checker = checker(source);
        assertFalse(checker.check(), source);
        assertTrue(checker.getErrors().stream().anyMatch(m -> m.contains(diagnostic)),
                () -> source + ": " + checker.getErrors());
    }

    @Test void acceptsBalancedGroupsAndSiblingGroups() throws Exception {
        for (String source : List.of("();", "((1));", "(1)+(2);", "(((1)+2)*(3));")) accepts(source);
    }

    @Test void acceptsEmptyNestedAndSiblingBlocks() throws Exception {
        for (String source : List.of("loop {}", "loop {loop {}}", "loop {} loop {}",
                "if ((1)) {loop {break;}} else {continue;}")) accepts(source);
    }

    @Test void rejectsMissingGroupClosersAtDifferentDepths() throws Exception {
        for (String source : List.of("(;", "((1);", "(1)+(2;", "loop {let #x = (1;}"))
            rejects(source, source.contains("}") ? "Unexpected [}]" : "Could not find matched pair");
    }

    @Test void rejectsMissingBlockClosersAtDifferentDepths() throws Exception {
        for (String source : List.of("loop {", "loop {loop {}", "loop {} loop {", "loop {let #x = (1);"))
            rejects(source, "Could not find matched pair");
    }

    @Test void rejectsStrayAndSurplusClosers() throws Exception {
        for (String source : List.of(");", "(1));", "loop {} ) ;")) rejects(source, "Unexpected [)]");
        for (String source : List.of("}", "loop {}}", "loop {} }")) rejects(source, "Unexpected [}]");
    }

    @Test void rejectsCrossedGroupsAndBlocks() throws Exception {
        rejects("loop {let #x = (1;});", "Unexpected [}]");
        rejects("(1) {);}", "Unexpected [)]");
    }

    @Test void reportsOpeningLocationForUnclosedGroup() throws Exception {
        rejects("\n  (1;", "Could not find matched pair for [(] found on line [2] column [3]");
    }

    @Test void reportsOpeningLocationForUnclosedBlock() throws Exception {
        rejects("\nloop {", "Could not find matched pair for [{] found on line [2] column [6]");
    }

    @Test void checksNeighborsInsideNestedStructures() throws Exception {
        rejects("loop {let #x = ((1 + ));}", "Unexpected [)] found after [+]");
        rejects("loop {loop {break 1;}}", "Unexpected [1] found after [break]");
    }

    @Test void checksNeighborsAfterBalancedStructures() throws Exception {
        rejects("(1) 2;", "Unexpected [2] found after [)]");
        rejects("loop {} let;", "Unexpected [;] found after [let]");
    }

    @Test void ignoresDelimiterCharactersInStringsAndComments() throws Exception {
        accepts("loop {let $x = `({)}`; // ) }\n}");
    }

    @Test void checksAreRepeatableWithoutAccumulatingErrors() throws Exception {
        SyntaxChecker checker = checker("((1);");
        assertFalse(checker.check());
        List<String> first = new ArrayList<>(checker.getErrors());
        assertFalse(checker.check());
        assertEquals(first, checker.getErrors());
        SyntaxChecker valid = checker("loop {break;}");
        assertTrue(valid.check());
        assertTrue(valid.check());
        assertTrue(valid.getErrors().isEmpty());
    }

    @Test void constructorRejectsEmptyAndNullTokenLists() {
        assertThrows(Exception.class, () -> new SyntaxChecker(List.of()));
        assertThrows(Exception.class, () -> new SyntaxChecker(null));
    }
}
