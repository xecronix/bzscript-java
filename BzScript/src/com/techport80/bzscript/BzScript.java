package com.techport80.bzscript;

import java.util.List;

public class BzScript {
	
	public static void main() throws Exception {
		//String bzcode = "println(`Hi kids.  Your Dad Loves You!`);printnum(---+++-30);";
		//String bzcode = "println(inputs());";
		//String bzcode = "println(midstr(`abcd`, 1, 3));nl();nl();nl();";
		//String bzcode = "printnum(strcomp(`same`, `same`));nl();";
		String bzcode = "let #stop = 0;\r\n"
				+ "let #count = 0;\r\n"
				+ "let $odd = ` is odd`;\r\n"
				+ "let $even = ` is even`;\r\n"
				+ "let $current = $odd;\r\n"
				+ "\r\n"
				+ "loop { if (#count == #stop) {break;} \r\n"
				+ "	// print the output\r\n"
				+ "	printnum(#count + 1);\r\n"
				+ "	println($current);\r\n"
				+ "\r\n"
				+ "	// reset for next loop\r\n"
				+ "	#count = #count + 1;\r\n"
				+ "	if (strcomp($current, $even))\r\n"
				+ "	{\r\n"
				+ "		$current = $odd;\r\n"
				+ "	}\r\n"
				+ "	else\r\n"
				+ "	{\r\n"
				+ "		$current = $even;\r\n"
				+ "	}\r\n"
				+ "}";

		
		Lexer lex = new Lexer();
		List<AstToken> tokens = lex.getTokens(bzcode, "embedded");
		
		SyntaxChecker c = new SyntaxChecker(tokens);
		if (!c.check()) {
			for (int i = 0; i < c.msg.size(); i++) {
				System.out.println(c.msg.get(i));
			}
		} 

		AstToken tree = Parser.parse(tokens);
		//System.out.println(tree.toJSON());
		StringBuilder sb = new StringBuilder();
		tree.serialize(true, sb);
		System.out.println(sb.toString());
		
		ContextChecker context = new ContextChecker(tree);
		context.check();
		
		Evaluator e = new Evaluator();
		e.eval(tree);
	}
}
