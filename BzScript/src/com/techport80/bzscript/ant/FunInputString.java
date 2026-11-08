package com.techport80.bzscript.ant;

import java.util.Scanner;

import com.techport80.bzscript.*;

public class FunInputString implements IAnt{
	
	Evaluator evaluator;
	public FunInputString(Evaluator e){
		evaluator = e;
	}
	
	public EvalMsg exe(AstToken t) throws Exception{
		EvalMsg evalMsg = new EvalMsg();
		Scanner scanner = new Scanner(System.in); 
		evalMsg.value.setString(scanner.nextLine());
		return evalMsg;
	}
}
