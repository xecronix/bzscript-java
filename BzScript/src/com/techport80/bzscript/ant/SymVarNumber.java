package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class SymVarNumber implements IAnt{
	
	Evaluator evaluator;
	public SymVarNumber(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		throw new Exception("SymVarNumber Not Implemented.");
	}
}
