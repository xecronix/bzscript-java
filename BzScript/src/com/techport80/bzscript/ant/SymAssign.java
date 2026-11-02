package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class SymAssign implements IAnt{
	
	Evaluator evaluator;
	public SymAssign(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		throw new Exception("SymAssign Not Implemented.");
	}
}
