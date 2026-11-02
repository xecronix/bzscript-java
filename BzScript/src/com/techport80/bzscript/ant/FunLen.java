package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class FunLen implements IAnt{
	
	Evaluator evaluator;
	public FunLen(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = evaluator.eval(t.tokens.getFirst());
		EvalMsg retval = new EvalMsg();
		retval.value.setDouble(evalMsg.value.getString().length());
		return retval;
	}
}
