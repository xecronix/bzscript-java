package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class SymVarString implements IAnt{
	
	Evaluator evaluator;
	public SymVarString(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		throw new Exception("SymVarString Not Implemented.");
	}
}
