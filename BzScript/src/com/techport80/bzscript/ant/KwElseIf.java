package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwElseIf implements IAnt{
	
	Evaluator evaluator;
	KwIf kwIfAnt;
	public KwElseIf(Evaluator e){
		evaluator = e;
		kwIfAnt = new KwIf(e);
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		return kwIfAnt.exe(t);
	}
}
