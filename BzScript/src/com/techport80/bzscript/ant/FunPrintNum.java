package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class FunPrintNum implements IAnt {
	Evaluator evaluator;
	
	public FunPrintNum(Evaluator e) {
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception {
		
		for (AstToken child : t.tokens) {
			EvalMsg m = evaluator.eval(child);
			System.out.print(m.value.getDouble());
		}
		return new EvalMsg();
	}
}
