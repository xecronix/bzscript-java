package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwContinue implements IAnt{
	
	Evaluator evaluator;
	public KwContinue(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		throw new Exception("KwContinue Not Implemented.");
	}
}
