package com.techport80.bzscript;
import com.techport80.bzscript.Language.*;
import com.techport80.bzscript.ant.*;
import java.util.*;

public class Evaluator {
	Map<Bz.Kind, IAnt> ants;
	public Evaluator() {
		ants = new HashMap<Bz.Kind, IAnt>();
		ants.put(Bz.Kind.LANG_FUN, new LangFun(this));
		ants.put(Bz.Kind.LITERAL_STRING, new LiteralString(this));
		ants.put(Bz.Kind.LITERAL_NUMBER, new LiteralNumber(this));
		ants.put(Bz.Kind.UNARY_NEGATIVE, new UnaryNegative(this));
		ants.put(Bz.Kind.UNARY_POSITIVE, new UnaryPositive(this));
		ants.put(Bz.Kind.VAR_NUMBER, new SymVarNumber(this));
		ants.put(Bz.Kind.VAR_STRING, new SymVarString(this));
		ants.put(Bz.Kind.BLOCK_OPEN, new SymBlockOpen(this));
		ants.put(Bz.Kind.ADDITION, new SymAddition(this));
		ants.put(Bz.Kind.SUBTRACTION, new SymSubtraction(this));
		ants.put(Bz.Kind.MULTIPLY, new SymMultiply(this));
		ants.put(Bz.Kind.DIVIDE, new SymDivide(this));
		ants.put(Bz.Kind.EXPONENT, new SymExponent(this));
		ants.put(Bz.Kind.ASSIGN, new SymAssign(this));
		ants.put(Bz.Kind.COMPARE_EQ, new SymCompareEq(this));
		ants.put(Bz.Kind.COMPARE_GREATER, new SymCompareGreater(this));
		ants.put(Bz.Kind.COMPARE_LESS, new SymCompareLess(this));
		ants.put(Bz.Kind.COMPARE_GREATER_EQ, new SymCompareGreaterEq(this));
		ants.put(Bz.Kind.COMPARE_LESS_EQ, new SymCompareLessEq(this));
		ants.put(Bz.Kind.COMPARE_NOT_EQ, new SymCompareNotEq(this));
		ants.put(Bz.Kind.KW_LET, new KwLet(this));
		ants.put(Bz.Kind.KW_IF, new KwIf(this));
		ants.put(Bz.Kind.KW_ELSE_IF, new KwElseIf(this));
		ants.put(Bz.Kind.KW_ELSE, new KwElse(this));
		ants.put(Bz.Kind.KW_LOOP, new KwLoop(this));
		ants.put(Bz.Kind.KW_CONTINUE, new KwContinue(this));
		ants.put(Bz.Kind.KW_BREAK, new KwBreak(this));
	}
	
	public EvalMsg eval(AstToken t) throws Exception{
		IAnt ant = ants.get(t.kind);
		if (ant == null) {
			throw new Exception ("Could not find Ant for token [%s]".formatted(t.toJSON()));
		}
		return ant.exe(t);
	}
}
