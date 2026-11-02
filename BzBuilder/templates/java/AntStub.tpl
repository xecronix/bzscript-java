package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;

public class {=eval_handler:} implements IAnt{
	
	Evaluator evaluator;
	public {=eval_handler:}(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		throw new Exception("{=eval_handler:} Not Implemented.");
	}
}
