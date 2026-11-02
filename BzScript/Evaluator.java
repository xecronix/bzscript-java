package com.techport80.bzscript;
import com.techport80.bzscript.Language.Bz;
import com.techport80.bzscript.Language.Bz.Kind;
import com.techport80.bzscript.ant.*;
import java.util.*;

public class Evaluator {
	Map<Bz.Kind, IAnt> ants;
	public Evaluator() {
		ants = new HashMap<Bz.Kind, IAnt>();
		ants.put(Bz.Kind.LANG_FUN, new LangFun(this));   
		ants.put(Kind.BLOCK_OPEN, new SymBlockOpen(this));
		ants.put(Kind.LITERAL_STRING, new LiteralString(this));
		ants.put(Kind.LITERAL_NUMBER, new LiteralNumber(this));
		ants.put(Kind.UNARY_NEGATIVE, new UnaryNegative(this));
		ants.put(Kind.UNARY_POSITIVE, new UnaryPositive(this));
		
	}
	
	public EvalMsg eval(AstToken t) throws Exception{
		IAnt ant = ants.get(t.kind);
		if (ant == null) {
			throw new Exception ("Could not find Ant for token [%s]".formatted(t.toJSON()));
		}
		return ant.exe(t);
	}
}
