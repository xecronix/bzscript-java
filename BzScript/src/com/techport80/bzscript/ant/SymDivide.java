package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class SymDivide implements IAnt{
	
	Evaluator evaluator;
	public SymDivide(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception {
		EvalMsg evalMsg = evaluator.eval(t.tokens.get(0));
		double l = evalMsg.value.getDouble();
		evalMsg = evaluator.eval(t.tokens.get(1));
		double r = evalMsg.value.getDouble();
		if (r == 0) {throw new Exception("Divide by 0 Error");}
		EvalMsg retval = new EvalMsg();
		retval.value.setDouble(l/r);
		return retval;
	}
}
