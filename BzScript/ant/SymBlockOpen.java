package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class SymBlockOpen implements IAnt {
	Evaluator evaluator;
	public SymBlockOpen(Evaluator e) {
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		
		for (AstToken child : t.tokens) {
			evaluator.eval(child);			
		}

		return evalMsg;
	}
}
