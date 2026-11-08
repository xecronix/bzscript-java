package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class KwIf implements IAnt{
	
	Evaluator evaluator;
	public KwIf(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = evaluator.eval(t.tokens.getFirst());
		if (evalMsg.value.getDouble()==1) {
			evalMsg = evaluator.eval(t.tokens.get(1));
		}
		else {
			evalMsg.flow = EvalMsg.Flow.ELSEIF;
		}
		return evalMsg;
	}
}
