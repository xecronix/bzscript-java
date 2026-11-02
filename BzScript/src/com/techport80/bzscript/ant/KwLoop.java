package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwLoop implements IAnt{
	
	Evaluator evaluator;
	public KwLoop(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		throw new Exception("KwLoop Not Implemented.");
	}
}
