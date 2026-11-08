package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwContinue implements IAnt{
	
	Evaluator evaluator;
	public KwContinue(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		evalMsg.flow = EvalMsg.Flow.CONTINUE;
		return evalMsg;
	}
}
