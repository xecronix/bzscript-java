package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwLet implements IAnt{
	
	Evaluator evaluator;
	public KwLet(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		
		int top = evaluator.state.frameStack.size() -1;
		RuntimeFrame f = evaluator.state.frameStack.get(top);
		AstToken variable = t.tokens.get(0).tokens.get(0); 
		BzValue v = f.values.get(variable.name);		
		if (v != null) {
			throw new Exception("Can not redeclare variable [%s] on line [%d] column [%d]"
					.formatted(variable.name, variable.lineNumber, variable.columnNumber));
					
		}
		
		v = new BzValue();
		f.values.put(variable.name, v);
		
		return evaluator.eval(t.tokens.get(0));
	}
}


