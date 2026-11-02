package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class FunStrComp implements IAnt{
	
	Evaluator evaluator;
	public FunStrComp(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = evaluator.eval(t.tokens.get(0));
		String str1 = evalMsg.value.getString();
		evalMsg = evaluator.eval(t.tokens.get(1));
		String str2 = evalMsg.value.getString();
		
		EvalMsg retval = new EvalMsg();
		retval.value.setDouble(0);
		if (str1.compareTo(str2) == 0) {
			retval.value.setDouble(1);	
		}
		
		return retval;
	}
}
