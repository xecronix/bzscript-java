package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class FunPrint implements IAnt{
	
	Evaluator evaluator;
	public FunPrint(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception {
		EvalMsg evalMsg = new EvalMsg();
		for (AstToken child : t.tokens) {
			evalMsg = evaluator.eval(child);
			System.out.print(evalMsg.value.getString());
		} 
		return evalMsg;
	}
}
