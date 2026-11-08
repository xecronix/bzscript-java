package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class FunPrintLn implements IAnt {
	Evaluator evaluator;
	
	public FunPrintLn(Evaluator e) {
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception {
		EvalMsg m =new EvalMsg();
		for (AstToken child : t.tokens) {
			m = evaluator.eval(child);
			System.out.println(m.value.getString());
		}
		return m;
	}
}
