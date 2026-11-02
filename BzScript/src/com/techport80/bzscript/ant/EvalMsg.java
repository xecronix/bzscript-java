package com.techport80.bzscript.ant;

public class EvalMsg {
	public static enum MessageKind{
		EMPTY,
		RETURN,
		ERROR
	};
	
	public static enum Flow{
		NORMAL,
		BREAK,
		CONTINUE
	};
	
	
	public MessageKind kind;
	public BzValue value;
	public String message;
	public Flow flow; 
	public EvalMsg() {
		kind = MessageKind.EMPTY ;
		flow = Flow.NORMAL;
		message = "";
		value = new BzValue();
	}
}
