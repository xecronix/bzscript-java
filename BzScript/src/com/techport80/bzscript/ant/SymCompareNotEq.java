package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class SymCompareNotEq implements IAnt{
	
	Evaluator evaluator;
	public SymCompareNotEq(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception {
		EvalMsg evalMsg = evaluator.eval(t.tokens.get(0));
		double l = evalMsg.value.getDouble();
		evalMsg = evaluator.eval(t.tokens.get(1));
		double r = evalMsg.value.getDouble();
		EvalMsg retval = new EvalMsg();
		double c = 0;
		if (l != r) {c = 1;}
		retval.value.setDouble(c);
		return retval;
	}
}
