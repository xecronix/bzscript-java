package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwLoop implements IAnt{
	
	Evaluator evaluator;
	public KwLoop(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		while (evalMsg.flow != EvalMsg.Flow.BREAK) {
			evalMsg = evaluator.eval(t.tokens.getFirst());
		}
		evalMsg.flow = EvalMsg.Flow.NORMAL;
		return evalMsg;
	}
}
