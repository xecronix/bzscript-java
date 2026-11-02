package com.techport80.bzscript;

import com.techport80.jlistcursor.*;
import java.util.ArrayList;
import java.util.List;

import com.techport80.bzscript.Language.Bz;

public class Lexer {
	
	private boolean isNumberLiteral(String str) throws Exception{
		boolean dotFound = false;
		boolean digitFound = false;
		
		for (int i = 0; i < str.length(); i++) {
			Character ch = str.charAt(i);
			if (ch == '.' && dotFound == false) {
				dotFound = true;
			}
			else if (Character.isDigit(ch)) {
				digitFound = true;
			}
			else {
				return false;
			}
		}
		
		return digitFound;			
	}
	
	public void Atomize(List<AstToken> buffer, String bzSourceCode, String fileName) {
		int lineNumber = 1;
		int colNumber = 1;
		
		for (int i = 0; i < bzSourceCode.length(); i++) {
			Character c = bzSourceCode.charAt(i);
			AstToken t = new AstToken();
			
			t.lineNumber = lineNumber;
			t.columnNumber = colNumber;
			t.fileName = fileName;
			
			colNumber++;
			
			if (c == '\n') {
				t.kind = Bz.Kind.EOL;
				colNumber = 1;
				lineNumber++;
			}
			
			else if (Character.isWhitespace(c)) {
				t.kind = Bz.Kind.WHITE_SPACE;
			}
			
			t.name += c;
			buffer.add(t);
		}
	}
	
	public void doCompress(Cursor<AstToken> cursor, List<AstToken> targetBuffer) throws Exception {
		while (true) { 
			AstToken current  = cursor.current();
			if (current.kind == Bz.Kind.WHITE_SPACE || current.kind == Bz.Kind.EOL ) {
				// do nothing
			}
			else if (current.kind == Bz.Kind.RESERVED) {
				throw new Exception("Reserved [%s] is not allowed on line [%d] column [%d]".formatted( 
						current.name, current.lineNumber, current.columnNumber));
			}
			else if ((current.kind== Bz.Kind.VAR_NUMBER || current.kind== Bz.Kind.VAR_STRING) && current.name.length() < 2) {
				throw new Exception("Variables require a name [%d] column [%d]".formatted( 
						current.lineNumber, current.columnNumber));
			}
			else if (current.kind== Bz.Kind.WORD || current.kind== Bz.Kind.UNKNOWN) {
				throw new Exception("Unidentifed token [%s] on line [%d] column [%d]".formatted( 
						current.name, current.lineNumber, current.columnNumber));
			}
			else {
				targetBuffer.add(current);
			}
				
			if (!cursor.next()) {break;}
		}
	}
	
	public void doStringAndComment(Cursor<AstToken> cursor, List<AstToken> targetBuffer) throws Exception {
		boolean stringOpen = false;
		boolean commentOpen = false;
		
		AstToken stringStart = new AstToken();
		while (true) {
			AstToken current = cursor.current();
			AstToken peek = cursor.tryPeek();
			boolean didPeek = peek != null;
			
			if (commentOpen) {
				if (current.kind == Bz.Kind.EOL) {
					targetBuffer.add(current);
					commentOpen = false;
				}
				// else do nothing.
			}
			else if (current.kind == Bz.Kind.STRING_DELIMITER && !stringOpen){
				stringStart = current;
				stringStart.name = "";
				stringStart.kind = Bz.Kind.LITERAL_STRING;
				stringOpen = true;
			}
			else if (current.kind == Bz.Kind.COMMENT && !stringOpen){
				commentOpen = true;
			}
			else if (current.kind == Bz.Kind.STRING_DELIMITER && stringOpen){
				if (didPeek && peek.kind == Bz.Kind.STRING_DELIMITER) {
					cursor.next();
					stringStart.name += Bz.symStringDelimiter;
				}
				else {
					stringOpen = false;
					targetBuffer.add(stringStart);
				}
			}
			else if (stringOpen) {
				stringStart.name += current.name;
			}
			else {
				targetBuffer.add(current);
			}
			
			if (!cursor.next()) {break;}
		}
		
		if (stringOpen) {
			throw new Exception("Did not find closing %s for string starting on line[%d] column[%d]".formatted(
								Bz.symStringDelimiter, stringStart.lineNumber, stringStart.columnNumber));
		}
	}
	
	public void doWords(Cursor<AstToken> cursor, List<AstToken> targetBuffer) throws Exception {
		
		AstToken word = cursor.current();
		boolean openWord = false;
		boolean newWord = false;
		while (true)
		{
			AstToken c = cursor.current();
			
			if (c.kind == Bz.Kind.UNKNOWN && !openWord ) {
				word = c;
				openWord = true;
			}
			else if (c.kind != Bz.Kind.UNKNOWN && openWord){
				newWord = true;
				openWord = false;
			}
			else if (openWord) {
				word.name += c.name;
			}
			else {
				targetBuffer.add(c);
			}
			
			int lastTargetIndex = targetBuffer.size() - 1;
			Bz.Kind lastKind = null;
			if (lastTargetIndex >= 0) {
				lastKind = targetBuffer.get(lastTargetIndex).kind;
			}
			if (newWord) {
				// check to see if this is a keyword or function
				Bz.Kind k = Bz.wordDef.get(word.name);
				if (lastKind != null && 
						(lastKind == Bz.Kind.VAR_NUMBER || lastKind == Bz.Kind.VAR_STRING )) {
					if (k != null) {
						throw new Exception ("Varible names [%s] cannot be keywords or built in functions. line [%d] col [%d]".formatted(
								word.name, word.lineNumber, word.columnNumber));
					}
					Character ch = word.name.charAt(0);
					if (Character.isDigit(ch) == false && ch != '.') {
						// we're not adding this to the buffer because it's already in the buffer.
						// just append the name to the $ or # that's already there.
						targetBuffer.get(lastTargetIndex).name += word.name;
						newWord = false;
					}
					else {
						throw new Exception("Varible names cannot start with a . or a digit line[%d] column[%d]".formatted(
								word.lineNumber, word.columnNumber));
					}
				}
				// in this case word is a keyword or built in function.
				else if (k != null){
					word.kind = k;
					targetBuffer.add(word);
					newWord = false;
				}
				// not a keyword or a variable.  Since we don't have user defined 
				// types or functions yet, this should be a number.
				else if (isNumberLiteral(word.name)) {
					word.kind = Bz.Kind.LITERAL_NUMBER;
					targetBuffer.add(word);
					newWord = false;
				}
				// else if user defined symbol {}
				
				else {
					throw new Exception("Could not identify word [%s] found at line[%d] column[%d]".formatted(
							word.name, word.lineNumber, word.columnNumber));
				}
				
				targetBuffer.add(c);
			}
			
			if (newWord) {
				throw new Exception("[BzScript Err] Could not identify word found at line[%d] column[%d]".formatted(
						word.lineNumber, word.columnNumber));
			}
			
			if (!cursor.next()) {break;}
		}
		
		if (openWord) {
			throw new Exception("Found End of file while attempting to identify [%s] line [%d] column [%d]".formatted(
					word.name, word.lineNumber, word.columnNumber));
		}
	}
	
	public void doSymbols(Cursor<AstToken> cursor, List<AstToken> targetBuffer) throws Exception {
		while (true) {
			AstToken current = cursor.current();
			AstToken peek = cursor.tryPeek();
			boolean didPeek = peek != null;
			boolean symbolFound = false;
			Bz.Kind k = null;
			
			if (didPeek) {
				String lookup = current.name + peek.name;
				k = Bz.symbolDef.get(lookup);
				if (k != null) {
					current.name += peek.name;
					current.kind = k;
					targetBuffer.add(current);
					cursor.next();
					symbolFound = true;
				}
			}
			
			if (!symbolFound) {
				k = Bz.symbolDef.get(current.name);
				if (k != null) {
					current.kind = k;
				}
				targetBuffer.add(current);
			}
			

			if (!cursor.next()) {break;}
		}
	}
	
	public void doUnary(Cursor<AstToken> cursor, List<AstToken> targetBuffer) throws Exception {
		cursor.fastForward();
		List<Bz.Kind> unaryIdentifiers = new ArrayList<Bz.Kind>();
		unaryIdentifiers.add(Bz.Kind.ADDITION);
		unaryIdentifiers.add(Bz.Kind.SUBTRACTION);
		unaryIdentifiers.add(Bz.Kind.MULTIPLY);
		unaryIdentifiers.add(Bz.Kind.DIVIDE);
		unaryIdentifiers.add(Bz.Kind.ASSIGN);
		unaryIdentifiers.add(Bz.Kind.COMPARE_EQ);
		unaryIdentifiers.add(Bz.Kind.COMPARE_GREATER);
		unaryIdentifiers.add(Bz.Kind.COMPARE_LESS);
		unaryIdentifiers.add(Bz.Kind.COMPARE_GREATER_EQ);
		unaryIdentifiers.add(Bz.Kind.COMPARE_LESS_EQ);
		unaryIdentifiers.add(Bz.Kind.COMPARE_NOT_EQ);
		unaryIdentifiers.add(Bz.Kind.EXPONENT);
		unaryIdentifiers.add(Bz.Kind.LIST_SEPARATOR);
		unaryIdentifiers.add(Bz.Kind.GROUP_OPEN);
				
		while (true) {
			AstToken c = cursor.current();
			AstToken b = cursor.tryRecall();
			boolean didRecall = b != null;
			
			if (didRecall){
				if (c.kind == Bz.Kind.ADDITION || c.kind == Bz.Kind.SUBTRACTION) {
					if (unaryIdentifiers.contains(b.kind)){
						if (c.kind == Bz.Kind.ADDITION) {
							c.kind = Bz.Kind.UNARY_POSITIVE;
						}
						else {
							c.kind = Bz.Kind.UNARY_NEGATIVE;
						}
					}
				}
			}
			else {
				if (c.kind == Bz.Kind.ADDITION || c.kind == Bz.Kind.SUBTRACTION){
					throw new Exception("Unexpected [%s] at start of input on line [%d] column [%d]"
							.formatted(c.name, c.lineNumber, c.columnNumber));				
				}
			}
			if (!cursor.back()) {break;}
		}
		
		while (true) {
			targetBuffer.add(cursor.current());
			if (!cursor.next()) {break;}
		}
	}
	
	public List<AstToken> getTokens(String bzSourceCode, String fileName) throws Exception{
		List<AstToken> buffer = new ArrayList<AstToken>();
		List<AstToken> backBuffer = new ArrayList<AstToken>();
		List<AstToken> workBuffer;
		List<AstToken> targetBuffer;
		boolean bufferFlipped = false;
		String[] actions = {"doSymbols", "doStringAndComment", "doWords", "doCompress", "doUnary"};

		Atomize(buffer, bzSourceCode, fileName);
		workBuffer = buffer;
		targetBuffer = backBuffer;
		
		for (int i = 0; i < actions.length; i++)
		{
			String action = actions[i];
			
			if (workBuffer.size() == 0){
				throw new Exception("[%s] workBuffer was empty. No Tokens available to lex".formatted(action));
			}
			Cursor<AstToken> cursor = new Cursor<AstToken>(workBuffer);
			
			if (action.compareTo("doSymbols") == 0){
				doSymbols(cursor, targetBuffer);
			}
			else if (action.compareTo("doStringAndComment") == 0){
				doStringAndComment(cursor, targetBuffer);
			}
			else if (action.compareTo("doCompress") == 0){
				doCompress(cursor, targetBuffer);
			}
			else if (action.compareTo("doWords") == 0){
				doWords(cursor, targetBuffer);
			}
			else if (action.compareTo("doUnary") == 0){
				doUnary(cursor, targetBuffer);
			}
			
			cursor = null;
			if (bufferFlipped) {
				workBuffer = buffer;
				backBuffer.clear();
				targetBuffer = backBuffer;
				bufferFlipped = false;
			}
			else {
				workBuffer = backBuffer;
				buffer.clear();
				targetBuffer = buffer;
				bufferFlipped = true;
			}
		}
		
		return workBuffer;
	}
}
