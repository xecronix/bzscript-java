package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class SymExponent implements IAnt{
	
	Evaluator evaluator;
	public SymExponent(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception {
		EvalMsg evalMsg = evaluator.eval(t.tokens.get(0));
		double l = evalMsg.value.getDouble();
		evalMsg = evaluator.eval(t.tokens.get(1));
		double r = evalMsg.value.getDouble();
		
		EvalMsg retval = new EvalMsg(); 
		retval.value.setDouble(Math.pow(l, r));
		return retval;
	}
}
