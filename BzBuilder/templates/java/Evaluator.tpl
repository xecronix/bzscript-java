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
		ants.put(Bz.Kind.UNARY_POSITIVE, new UnaryPositive(this));{@antBzLanguage;
		ants.put(Bz.Kind.{=kind:}, new {=eval_handler:}(this));:}
	}
	
	public EvalMsg eval(AstToken t) throws Exception{
		IAnt ant = ants.get(t.kind);
		if (ant == null) {
			throw new Exception ("Could not find Ant for token [%s]".formatted(t.toJSON()));
		}
		return ant.exe(t);
	}
}
