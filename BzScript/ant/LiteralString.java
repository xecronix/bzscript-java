package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class LiteralString implements IAnt{
	
	Evaluator evaluator;
	public LiteralString(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		evalMsg.value.setString(t.name);
		return evalMsg;
	}

}
