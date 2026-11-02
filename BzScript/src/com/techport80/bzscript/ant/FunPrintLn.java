package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class FunPrintLn implements IAnt {
	Evaluator evaluator;
	
	public FunPrintLn(Evaluator e) {
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception {
		
		for (AstToken child : t.tokens) {
			EvalMsg m = evaluator.eval(child);
			System.out.println(m.value.getString());
		}
		return new EvalMsg();
	}
}
