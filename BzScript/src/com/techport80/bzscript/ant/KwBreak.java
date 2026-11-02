package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwBreak implements IAnt{
	
	Evaluator evaluator;
	public KwBreak(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		throw new Exception("KwBreak Not Implemented.");
	}
}
