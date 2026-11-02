package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwLet implements IAnt{
	
	Evaluator evaluator;
	public KwLet(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		throw new Exception("KwLet Not Implemented.");
	}
}
