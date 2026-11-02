package com.techport80.jeagle;

import java.util.ArrayList;
import java.util.List;

import com.techport80.jlistcursor.*;

public class Lexer {
	private Cursor<Token> cursor;
	private List<Token> tokens;
	private boolean isGrouped = false;
	
	public void init(String template) {
		int lineNumber = 1;
		int columnNumber = 1;
		isGrouped = false;
		tokens = new ArrayList<Token>();
		
		for (int i = 0; i < template.length(); i++){
			char c = template.charAt(i);

			// create a new token for every char
			Token t = new Token();
			t.lineNumber = lineNumber;
			t.columnNumber = columnNumber;
			t.text += c;
			
			// add the token to the list of tokens
			tokens.add(t);
			
			if (c =='\n') {
				lineNumber++;
				columnNumber = 1;
			}
			else {
				columnNumber++;
			}
		}
	}
	
	private Token doSubstituteTagOpen() throws Exception{
		// Assemble and add the opening token
		Token retval = cursor.current();
		retval.text="";
		retval.kind = Token.Kind.OPEN_SUBSTITUTION;
		
		cursor.next(); // move to =

		// the first iteration consumes the =
		while (cursor.next()) {
			Token c = cursor.current();
			Token p = cursor.tryPeek();
			boolean didPeek = p != null;
			
			if (didPeek && 
					c.text.compareTo(":") == 0 &&
					p.text.compareTo("}") == 0) {
				retval.text = retval.text.trim();
				return retval;
			}
			else {
				retval.text += c.text;
			}
		}
		
		throw new Exception("Could not find closing token for substitute found on line [%d] column [%d]".formatted(retval.lineNumber, retval.columnNumber));
				
	}
	
	private Token doActionTagOpen() throws Exception{
		
		// first we need the current.text to be the key for the map.
		// so that means we want the cursor sitting on the opening { 
		// for the action tag {@.
		
		// in this version of Mighty Eagle we'll end the action tag with discarded ;
		// this means the tag can't contain a ; which is would be sort of silly 
		// to put a ; in a tag anyway.
		
		// Assemble and add the opening token
		Token retval = cursor.current();
		retval.text="";
		retval.kind = Token.Kind.OPEN_ACTION;
		
		cursor.next(); // move to @

		// the first iteration consumes the =
		while (cursor.next()) {
			Token c = cursor.current();
			if (c.text.compareTo(";") == 0) {
				return retval;
			}
			// fail a little faster here. blacklist 
			// some likely mistakes. Like a space, \n, :, or a }
			else if (" \n:}".contains(c.text)) {
				throw new Exception("Expected ; but found [%s] on line [%d] column [%d]".formatted(c.text, c.lineNumber, c.columnNumber));
			}
			else {
				retval.text += c.text;
			}
		}
		
		throw new Exception("Could not find closing token for action found on line [%d] column [%d]".formatted(retval.lineNumber, retval.columnNumber));
		
	}

	private void groupTokens(List<Token> newTokens, boolean actionTagOpen) throws Exception {
		
		Token newToken = new Token();
		while (true) {
			Token current = cursor.current();
			Token peek = cursor.tryPeek();
			boolean didPeek = peek != null;
			
			if (didPeek && current.text.compareTo("{") == 0 && peek.text.compareTo("=") == 0 ) {
				// add the token we've been accumulating. 
				if (newToken.text.length() > 0) {
					newTokens.add(newToken);
				}
				newToken = new Token();
				
				// add the substitute token
				newTokens.add(doSubstituteTagOpen());

				// at this point I'm dropping the closing substitution token.  It's not needed anymore.
				// we're on the closing tag's :
				// consume the :
				cursor.next(); 
				// now we're on the } and we'll advance again in a moment. 
			}
			else if (didPeek && current.text.compareTo("{") == 0 && peek.text.compareTo("@") == 0 ) {
				// add the token we've been accumulating. 
				if (newToken.text.length() > 0) {
					newTokens.add(newToken);
				}
				newToken = new Token();
				
				// add the action token
				newTokens.add(doActionTagOpen());
				
				// the cursor should be sitting on the ; advance it.
				if (!cursor.next()) {
					throw new Exception("Unexpected End of File found at while grouping action tag.");
				}
				
				// start descending
				groupTokens(newTokens, true);
			}
			else if (didPeek && current.text.compareTo(":") == 0 && peek.text.compareTo("}") == 0 ) {
				if (!actionTagOpen) {
					throw new Exception("Found closing token for an unopened action on line [%d] column [%d]".formatted(current.lineNumber, current.columnNumber));
				}
				
				// add the token we've been accumulating.
				if (newToken.text.length() > 0) {
					newTokens.add(newToken);
				}
				newToken = new Token();

				// add the closing tag to the list.
				current.kind = Token.Kind.CLOSE_ACTION;
				current.text+="}";
				newTokens.add(current);
				cursor.next(); // move to the }
				return;
			}
			else {
				newToken.text += current.text;
			}
						
			if (!cursor.next()) {
				if (newToken.text.length() > 0) {
					newTokens.add(newToken);
				}
				if (actionTagOpen) {
				    throw new Exception("Reached end of template without closing action :}");
				}
				break;
			}
		}
	}

	public Cursor<Token> getCursor() throws Exception{
		if(!isGrouped) {
			List<Token> newTokens = new ArrayList<Token>();
			if (tokens != null && tokens.size() > 0){
				cursor = new Cursor<Token>(tokens);
			}
			else {
				throw new Exception("Could not create cursor because there were no tokens.  Probably the template was empty during construction.");
			}
			
			groupTokens(newTokens, false);
			isGrouped = true;
			tokens = newTokens;
			cursor = null;
		}
		return new Cursor<Token>(tokens);
	}
		
	public Lexer(String template) {
		if (template.length() > 0){
			init(template);
		}
	}
}
