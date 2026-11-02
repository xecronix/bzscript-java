package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class UnaryPositive implements IAnt{
	
	Evaluator evaluator;
	public UnaryPositive(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = evaluator.eval(t.tokens.getFirst());
		return evalMsg;
	}

}
