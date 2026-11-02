package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class UnaryNegative implements IAnt{
	
	Evaluator evaluator;
	public UnaryNegative(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = evaluator.eval(t.tokens.getFirst());
		evalMsg.value.setDouble(evalMsg.value.getDouble() * -1);
		return evalMsg;
	}

}
