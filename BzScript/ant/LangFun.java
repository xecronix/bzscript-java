package com.techport80.bzscript.ant;

import java.util.HashMap;
import java.util.Map;

import com.techport80.bzscript.*;
import com.techport80.bzscript.Language.Bz;

public class LangFun implements IAnt{
	Evaluator evaluator;
	Map<String, IAnt> ants;
	public LangFun(Evaluator e) {
		evaluator = e;
		ants = new HashMap<String, IAnt>();
		ants.put(Bz.funPrintLn, new FunPrintLn(e));
		ants.put(Bz.funPrintNum, new FunPrintNum(e));
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		String fun =  t.name;
		IAnt ant = ants.get(fun);
		return ant.exe(t);
	}
}
