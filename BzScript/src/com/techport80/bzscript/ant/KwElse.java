package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwElse implements IAnt{
	
	Evaluator evaluator;
	public KwElse(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		return evaluator.eval(t.tokens.getFirst()); 
	}
}
