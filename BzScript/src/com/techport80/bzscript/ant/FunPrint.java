package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class FunPrint implements IAnt{
	
	Evaluator evaluator;
	public FunPrint(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception {
		
		for (AstToken child : t.tokens) {
			EvalMsg m = evaluator.eval(child);
			System.out.print(m.value.getString());
		}
		return new EvalMsg();
	}
}
