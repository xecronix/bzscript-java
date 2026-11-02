package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwIf implements IAnt{
	
	Evaluator evaluator;
	public KwIf(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		throw new Exception("KwIf Not Implemented.");
	}
}
