package com.techport80.bzscript;

import java.util.ArrayList;
import java.util.List;

import com.techport80.bzscript.Language.Bz;

public class KindGroups {
	public static List<Bz.Kind> getUnarys(){
		List<Bz.Kind> unaryKinds = new ArrayList<Bz.Kind>();
		unaryKinds.add(Bz.Kind.UNARY_NEGATIVE);
		unaryKinds.add(Bz.Kind.UNARY_POSITIVE);
		return unaryKinds;
	}

	public static List<Bz.Kind> getConditionalBlocks(){
		List<Bz.Kind> conditionalBlocks = new ArrayList<Bz.Kind>();
		conditionalBlocks.add(Bz.Kind.KW_IF);
		conditionalBlocks.add(Bz.Kind.KW_ELSE_IF);
		return conditionalBlocks;
	}
	
	public static List<Bz.Kind> getUnconditionalBlocks(){
		List<Bz.Kind> unconditionalBlocks = new ArrayList<Bz.Kind>();
		unconditionalBlocks.add(Bz.Kind.KW_LOOP);
		unconditionalBlocks.add(Bz.Kind.KW_ELSE);
		return unconditionalBlocks;
	}

	public static List<Bz.Kind> getAdditive(){
		List<Bz.Kind> addKinds = new ArrayList<Bz.Kind>();
		addKinds.add(Bz.Kind.ADDITION);
		addKinds.add(Bz.Kind.SUBTRACTION);
		return addKinds;
	}

	public static List<Bz.Kind> getMultiplicative(){
		List<Bz.Kind> multiKinds = new ArrayList<Bz.Kind>();
		multiKinds.add(Bz.Kind.MULTIPLY);
		multiKinds.add(Bz.Kind.DIVIDE);
		return multiKinds;
	}

	public static List<Bz.Kind> getExponentTerms(){
		List<Bz.Kind> exponentTerms = new ArrayList<Bz.Kind>();
		exponentTerms.add(Bz.Kind.LITERAL_NUMBER);
		exponentTerms.add(Bz.Kind.VAR_NUMBER);
		exponentTerms.add(Bz.Kind.LANG_FUN);
		exponentTerms.add(Bz.Kind.GROUP_OPEN);
		return exponentTerms;
		
	}
	
	public static List<Bz.Kind> getBooleans(){
		List<Bz.Kind> boolKinds = new ArrayList<Bz.Kind>();
		boolKinds.add(Bz.Kind.COMPARE_EQ);
		boolKinds.add(Bz.Kind.COMPARE_NOT_EQ);
		boolKinds.add(Bz.Kind.COMPARE_GREATER);
		boolKinds.add(Bz.Kind.COMPARE_GREATER_EQ);
		boolKinds.add(Bz.Kind.COMPARE_LESS);
		boolKinds.add(Bz.Kind.COMPARE_LESS_EQ);
		return boolKinds;
	}
}
