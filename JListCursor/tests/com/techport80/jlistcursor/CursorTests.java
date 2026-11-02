package com.techport80.jlistcursor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;


class CursorTests {

    @Test
    void currentStartsAtFirstItem() throws Exception {
        ArrayList<String> items = new ArrayList<>();
        items.add("one");
        items.add("two");

        Cursor<String> cursor = new Cursor<>(items);

        assertEquals("one", cursor.current());
    }
    
    @Test
    void nextMovesForwardAndStopsAtLastItem() throws Exception {
        ArrayList<String> items = new ArrayList<>();
        items.add("one");
        items.add("two");

        Cursor<String> cursor = new Cursor<>(items);

        assertTrue(cursor.hasMore());
        assertTrue(cursor.next());
        assertEquals("two", cursor.current());

        assertFalse(cursor.hasMore());
        assertFalse(cursor.next());
        assertEquals("two", cursor.current());
    }

    @Test
    void backMovesBackwardAndStopsAtFirstItem() throws Exception {
        ArrayList<String> items = new ArrayList<>();
        items.add("one");
        items.add("two");

        Cursor<String> cursor = new Cursor<>(items);
        cursor.fastForward();

        assertTrue(cursor.hasLess());
        assertTrue(cursor.back());
        assertEquals("one", cursor.current());

        assertFalse(cursor.hasLess());
        assertFalse(cursor.back());
        assertEquals("one", cursor.current());
    }

    @Test
    void peekAndRecallDoNotMoveTheCursor() throws Exception {
        ArrayList<String> items = new ArrayList<>();
        items.add("one");
        items.add("two");
        items.add("three");

        Cursor<String> cursor = new Cursor<>(items);
        assertTrue(cursor.next());

        assertEquals("three", cursor.tryPeek());
        assertEquals("two", cursor.current());

        assertEquals("one", cursor.tryRecall());
        assertEquals("two", cursor.current());
    }

    @Test
    void peekAndRecallReturnNullAtBoundaries() throws Exception {
        ArrayList<String> items = new ArrayList<>();
        items.add("one");
        items.add("two");

        Cursor<String> cursor = new Cursor<>(items);

        assertNull(cursor.tryRecall());
        assertEquals("one", cursor.current());

        cursor.fastForward();

        assertNull(cursor.tryPeek());
        assertEquals("two", cursor.current());
    }

    @Test
    void rewindAndFastForwardReachTheEndpoints() throws Exception {
        ArrayList<String> items = new ArrayList<>();
        items.add("one");
        items.add("two");
        items.add("three");

        Cursor<String> cursor = new Cursor<>(items);

        cursor.fastForward();
        assertEquals("three", cursor.current());

        cursor.rewind();
        assertEquals("one", cursor.current());

        assertTrue(cursor.next());
        assertEquals("two", cursor.current());
    }

    @Test
    void singleItemRemainsCurrentAfterEveryNavigationAttempt()
            throws Exception {
        ArrayList<String> items = new ArrayList<>();
        items.add("only");

        Cursor<String> cursor = new Cursor<>(items);

        assertFalse(cursor.hasMore());
        assertFalse(cursor.hasLess());
        assertFalse(cursor.next());
        assertFalse(cursor.back());
        assertNull(cursor.tryPeek());
        assertNull(cursor.tryRecall());
        assertEquals("only", cursor.current());

        cursor.fastForward();
        assertEquals("only", cursor.current());

        cursor.rewind();
        assertEquals("only", cursor.current());
    }
    
    @Test
    void changesToOriginalListDoNotAffectCursor() throws Exception {
        ArrayList<String> items = new ArrayList<>();
        items.add("one");
        items.add("two");

        Cursor<String> cursor = new Cursor<>(items);

        items.set(0, "changed");
        items.clear();
        items.add("replacement");

        assertEquals("one", cursor.current());
        assertTrue(cursor.next());
        assertEquals("two", cursor.current());
        assertFalse(cursor.next());
    }
}