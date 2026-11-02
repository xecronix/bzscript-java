package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwElseIf implements IAnt{
	
	Evaluator evaluator;
	public KwElseIf(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		throw new Exception("KwElseIf Not Implemented.");
	}
}
