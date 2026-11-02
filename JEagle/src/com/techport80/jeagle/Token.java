package com.techport80.jeagle;

public class Token {

	public static enum Kind{
		PHRASE, 
		OPEN_SUBSTITUTION,
		CLOSE_SUBSTITUTION,
		OPEN_ACTION,
		CLOSE_ACTION
		//KEY /* Key to the data or action in one of the maps */  I don't think this is needed ATM.  
	};
	
	// we're not going to hide these behind getters and setters.  
	public String text;
	public Kind kind;
	public int lineNumber;
	public int columnNumber;
	
	public Token() {
		this.text = "";
		this.kind = Token.Kind.PHRASE
				;
		this.lineNumber = 0;
		this.columnNumber = 0;		
	}
}
