package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class LiteralNumber implements IAnt{
	
	Evaluator evaluator;
	public LiteralNumber(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		double d = Double.parseDouble(t.name);
		evalMsg.value.setDouble(d);
		return evalMsg;
	}

}
