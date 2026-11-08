package com.techport80.bzscript.ant;

import com.techport80.bzscript.*;
import com.techport80.bzscript.Language.Bz;

public class SymBlockOpen implements IAnt {
	Evaluator evaluator;
	public SymBlockOpen(Evaluator e) {
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		try {
			RuntimeFrame frame = new RuntimeFrame(t);
			evaluator.state.frameStack.add(frame);
			for (AstToken child : t.tokens) {
				
				boolean isElse = child.kind == Bz.Kind.KW_ELSE_IF || child.kind == Bz.Kind.KW_ELSE; 
				
				if (isElse && evalMsg.flow != EvalMsg.Flow.ELSEIF) {
					continue;
				}
				
				evalMsg = evaluator.eval(child);
				if (evalMsg.flow == EvalMsg.Flow.BREAK || evalMsg.flow == EvalMsg.Flow.CONTINUE) {					
					return evalMsg ;
				}
			} 

			evalMsg.flow = EvalMsg.Flow.NORMAL;
		}
		finally {
			evaluator.state.frameStack.removeLast(); 
		}

		return evalMsg;
	}
}
