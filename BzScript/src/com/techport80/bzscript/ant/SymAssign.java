package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;
import com.techport80.bzscript.Language.Bz;

public class SymAssign implements IAnt{
	
	Evaluator evaluator;
	public SymAssign(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		AstToken left = t.tokens.get(0);
		EvalMsg evalMsg = evaluator.eval(t.tokens.get(1));
		int frameCount = evaluator.state.frameStack.size();
		while(frameCount != 0) {
			frameCount--;
			RuntimeFrame f = evaluator.state.frameStack.get(frameCount);			 
			BzValue v = f.values.get(left.name);
			if (v != null) {
				if(left.kind == Bz.Kind.VAR_NUMBER) {
					v.setDouble(evalMsg.value.getDouble());
				}
				else{
					v.setString(evalMsg.value.getString());
				}
				return evalMsg;
			}
		}
		throw new Exception("Could not find variable named [%s] on line [%d] column[%d]"
				.formatted(left.name, left.lineNumber, left.columnNumber));
	}
}
