package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class SymVarString implements IAnt{
	
	Evaluator evaluator;
	public SymVarString(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		int frameCount = evaluator.state.frameStack.size();
		while(frameCount != 0) {
			frameCount--;
			RuntimeFrame f = evaluator.state.frameStack.get(frameCount);			 
			if (f.values != null) {
				BzValue v = f.values.get(t.name);
				if (v != null) {
					evalMsg.value.setString(v.getString());
					return evalMsg;
				}
			}
		}
		throw new Exception("Could not find variable named [%s] on line [%d] column[%d]"
				.formatted(t.name, t.lineNumber, t.columnNumber));
	}
}
