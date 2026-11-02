package com.techport80.bzscript;

import java.util.List;

public class BzScript {
	
	public static void main() throws Exception {
		//String bzcode = "println(`Hi kids.  Your Dad Loves You!`);printnum(---+++-30);";
		//String bzcode = "println(inputs());";
		//String bzcode = "println(midstr(`abcd`, 1, 3));nl();nl();nl();";
		//String bzcode = "printnum(strcomp(`same`, `same`));nl();";
		String bzcode = "printnum(2^3^2);nl();";
		bzcode += "printnum(-2^-3^2);nl();";
		bzcode += "printnum(2--1);nl();";
		
		
		bzcode += "println(`End.`);";
		Lexer lex = new Lexer();
		List<AstToken> tokens = lex.getTokens(bzcode, "embedded");
		
		AstToken tree = Parser.parse(tokens);
		//System.out.println(tree.toJSON());
		
		Evaluator e = new Evaluator();
		e.eval(tree);
	}
}
