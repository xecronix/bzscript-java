package com.techport80.jeagle;

import com.techport80.jlistcursor.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techport80.bzdata.*;

public class JEagle {
	
	private Cursor<Token> cursor; 
	private Map<String, IActionRunner> actions;
	private Map<String, ISubstitutionRunner> substitutions;
	
	private void init() {
		actions = new HashMap<String, IActionRunner>();
		substitutions = new HashMap<String, ISubstitutionRunner>();
	}
	
	public JEagle(String template) throws Exception{
		init();
		Lexer lex = new Lexer(template);
		cursor = lex.getCursor();
	}
	
	private JEagle(Cursor<Token> cursor) throws Exception {
		init();
		this.cursor = cursor;
	}
	
	public void addAction(String key, IActionRunner action){
		actions.put(key, action);
	}

	public void addSubstitution(String key, ISubstitutionRunner runner) {
		substitutions.put(key, runner);
	}
	
	public void rewind() {
		this.cursor.rewind();
	}
	
	private Cursor<Token> actionSlice() throws Exception{
		int nestLevel = 1;
		List<Token> slice = new ArrayList<Token>();
		Token start = cursor.current();
		
		while (cursor.next()) {
			Token t = cursor.current();
			if (t.kind == Token.Kind.OPEN_ACTION) {
				nestLevel++;
				slice.add(t);
			}
			else if (t.kind == Token.Kind.CLOSE_ACTION) {
				nestLevel--;
				if (nestLevel == 0) {
					if (slice.size() == 0) {
						throw new EmptySliceException();
					}
					return new Cursor<Token>(slice);
				}
				else {
					slice.add(t);
				}
			}
			else {
				slice.add(t);
			}
		}
		
		throw new Exception("Could not find a matching closing tag for action opening on line [%d] column [%d]".formatted(start.lineNumber, start.columnNumber));
	}
	
	public String parse(BzRecord context) throws Exception{
		String retval = "";
		if (cursor == null) {
			throw new Exception("Cannot parse because cursor is null.");
		}
		while (true) {
			Token t = cursor.current();
			if (t.kind == Token.Kind.PHRASE) {
				retval += t.text;
			}
			else if (t.kind == Token.Kind.OPEN_SUBSTITUTION) {
				BzField f = context.getField(t.text); 
				ISubstitutionRunner runner = substitutions.get(t.text);
				if (runner != null) {
					retval += runner.run(f.getData());
				}
				else {
					retval += f.getData();
				}
			}
			else if (t.kind == Token.Kind.OPEN_ACTION) {
				IActionRunner action = actions.get(t.text);
				if (action == null) {
				    throw new Exception("Unknown action [%s] on line [%d] column [%d]"
				        .formatted(t.text, t.lineNumber, t.columnNumber));
				}

				try {
					Cursor<Token> actCursor = actionSlice();
					JEagle actionEagle = new JEagle(actCursor);
					retval += action.run(actionEagle, context);
				}
				catch (EmptySliceException e) {
					retval += action.run(null, context);
				}
			}
			else if (t.kind == Token.Kind.CLOSE_ACTION) {
				return retval;
			}
			if (!cursor.next()) {break;}
		}
		return retval;
	}
	
	
}
