package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class FunNewLine implements IAnt{
	
	Evaluator evaluator;
	public FunNewLine(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		System.out.println("");
		return evalMsg;
	}
}
