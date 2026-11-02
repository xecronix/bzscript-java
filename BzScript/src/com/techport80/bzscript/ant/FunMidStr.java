package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class FunMidStr implements IAnt{
	
	Evaluator evaluator;
	public FunMidStr(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = evaluator.eval(t.tokens.get(0));
		String subject = evalMsg.value.getString();
		evalMsg = evaluator.eval(t.tokens.get(1));
		int start = (int)evalMsg.value.getDouble();
		evalMsg = evaluator.eval(t.tokens.get(2));
		int end = (int)evalMsg.value.getDouble();
		
		EvalMsg retval = new EvalMsg();
		retval.value.setString(subject.substring(start, end));
		return retval;
	}
}
