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
		ants.put(Bz.funPrint, new FunPrint(evaluator));
		ants.put(Bz.funPrintLn, new FunPrintLn(evaluator));
		ants.put(Bz.funPrintNum, new FunPrintNum(evaluator));
		ants.put(Bz.funNewLine, new FunNewLine(evaluator));
		ants.put(Bz.funInputNumber, new FunInputNumber(evaluator));
		ants.put(Bz.funInputString, new FunInputString(evaluator));
		ants.put(Bz.funLen, new FunLen(evaluator));
		ants.put(Bz.funStrCat, new FunStrCat(evaluator));
		ants.put(Bz.funMidStr, new FunMidStr(evaluator));
		ants.put(Bz.funStrComp, new FunStrComp(evaluator));
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		String fun =  t.name;
		IAnt ant = ants.get(fun);
		return ant.exe(t);
	}
}
