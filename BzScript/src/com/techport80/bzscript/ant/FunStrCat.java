package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class FunStrCat implements IAnt{
	
	Evaluator evaluator;
	public FunStrCat(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg retval = new EvalMsg();
		StringBuilder sb = new StringBuilder();
		
		for (AstToken child : t.tokens) {
			EvalMsg evalMsg = evaluator.eval(child);
			sb.append(evalMsg.value.getString());
		}
		retval.value.setString(sb.toString());
		return retval;
	}
}
