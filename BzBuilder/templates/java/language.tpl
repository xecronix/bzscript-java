package com.techport80.bzscript;

import java.util.HashMap;
import java.util.Map;


public class Language {
	public static class Bz{
	
		public static enum Kind{
		    START_OF_INPUT,
			WORD,
			EOL,
			WHITE_SPACE,
			LITERAL_STRING,
			LITERAL_NUMBER,
			UNARY_NEGATIVE,
			UNARY_POSITIVE,
			{@bzenum;{=kind:},
			:}UNKNOWN 
		};	
	{@const;
		public static final String {=spelling_const:} = "{=spelling:}";:}
		
		public static final Map<String, Bz.Kind> symbolDef;
		static{
			Map<String, Bz.Kind> definitions = new HashMap<String, Bz.Kind>();	
		{@symmapping;
			definitions.put({=spelling_const:}, Bz.Kind.{=kind:});:}
			symbolDef = Map.copyOf(definitions); // create an immutable copy
		}
		
		public static final Map<String, Bz.Kind> wordDef;
		static{
			Map<String, Bz.Kind> definitions = new HashMap<String, Bz.Kind>();	
		{@wordmapping;
			definitions.put({=spelling_const:}, Bz.Kind.{=kind:});:}
			wordDef = Map.copyOf(definitions); // create an immutable copy
		}
	}
}
