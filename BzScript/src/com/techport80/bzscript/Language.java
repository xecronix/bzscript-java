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
			STATEMENT_END,
			LIST_SEPARATOR,
			VAR_NUMBER,
			VAR_STRING,
			GROUP_OPEN,
			GROUP_CLOSE,
			BLOCK_OPEN,
			BLOCK_CLOSE,
			ADDITION,
			SUBTRACTION,
			MULTIPLY,
			DIVIDE,
			EXPONENT,
			ASSIGN,
			COMPARE_EQ,
			COMPARE_GREATER,
			COMPARE_LESS,
			COMPARE_GREATER_EQ,
			COMPARE_LESS_EQ,
			COMPARE_NOT_EQ,
			COMMENT,
			STRING_DELIMITER,
			RESERVED,
			KW_LET,
			KW_IF,
			KW_ELSE_IF,
			KW_ELSE,
			KW_LOOP,
			KW_CONTINUE,
			KW_BREAK,
			LANG_FUN,
			UNKNOWN 
		};	
	
		public static final String symStatementEnd = ";";
		public static final String symListSeparator = ",";
		public static final String symVarNumber = "#";
		public static final String symVarString = "$";
		public static final String symGroupOpen = "(";
		public static final String symGroupClose = ")";
		public static final String symBlockOpen = "{";
		public static final String symBlockClose = "}";
		public static final String symAddition = "+";
		public static final String symSubtraction = "-";
		public static final String symMultiply = "*";
		public static final String symDivide = "/";
		public static final String symExponent = "^";
		public static final String symAssign = "=";
		public static final String symCompareEq = "==";
		public static final String symCompareGreater = ">";
		public static final String symCompareLess = "<";
		public static final String symCompareGreaterEq = ">=";
		public static final String symCompareLessEq = "<=";
		public static final String symCompareNotEq = "!=";
		public static final String symComment = "//";
		public static final String symStringDelimiter = "`";
		public static final String symExclamation = "!";
		public static final String symDoubleQuote = "\"";
		public static final String symPercent = "%";
		public static final String symAmpersand = "&";
		public static final String symSingleQuote = "'";
		public static final String symColon = ":";
		public static final String symQuestion = "?";
		public static final String symAt = "@";
		public static final String symBracketOpen = "[";
		public static final String symBracketClose = "]";
		public static final String symBackSlash = "\\";
		public static final String symPipe = "|";
		public static final String symTilde = "~";
		public static final String kwLet = "let";
		public static final String kwIf = "if";
		public static final String kwElseIf = "elseif";
		public static final String kwElse = "else";
		public static final String kwLoop = "loop";
		public static final String kwContinue = "continue";
		public static final String kwBreak = "break";
		public static final String funPrint = "print";
		public static final String funPrintLn = "println";
		public static final String funPrintNum = "printnum";
		public static final String funNewLine = "nl";
		public static final String funInputNumber = "inputn";
		public static final String funInputString = "inputs";
		public static final String funLen = "len";
		public static final String funStrCat = "strcat";
		public static final String funMidStr = "midstr";
		public static final String funStrComp = "strcomp";
		
		public static final Map<String, Bz.Kind> symbolDef;
		static{
			Map<String, Bz.Kind> definitions = new HashMap<String, Bz.Kind>();	
		
// Symbols
			definitions.put(symStatementEnd, Bz.Kind.STATEMENT_END);
			definitions.put(symListSeparator, Bz.Kind.LIST_SEPARATOR);
			definitions.put(symVarNumber, Bz.Kind.VAR_NUMBER);
			definitions.put(symVarString, Bz.Kind.VAR_STRING);
			definitions.put(symGroupOpen, Bz.Kind.GROUP_OPEN);
			definitions.put(symGroupClose, Bz.Kind.GROUP_CLOSE);
			definitions.put(symBlockOpen, Bz.Kind.BLOCK_OPEN);
			definitions.put(symBlockClose, Bz.Kind.BLOCK_CLOSE);
			definitions.put(symAddition, Bz.Kind.ADDITION);
			definitions.put(symSubtraction, Bz.Kind.SUBTRACTION);
			definitions.put(symMultiply, Bz.Kind.MULTIPLY);
			definitions.put(symDivide, Bz.Kind.DIVIDE);
			definitions.put(symExponent, Bz.Kind.EXPONENT);
			definitions.put(symAssign, Bz.Kind.ASSIGN);
			definitions.put(symCompareEq, Bz.Kind.COMPARE_EQ);
			definitions.put(symCompareGreater, Bz.Kind.COMPARE_GREATER);
			definitions.put(symCompareLess, Bz.Kind.COMPARE_LESS);
			definitions.put(symCompareGreaterEq, Bz.Kind.COMPARE_GREATER_EQ);
			definitions.put(symCompareLessEq, Bz.Kind.COMPARE_LESS_EQ);
			definitions.put(symCompareNotEq, Bz.Kind.COMPARE_NOT_EQ);
			definitions.put(symComment, Bz.Kind.COMMENT);
			definitions.put(symStringDelimiter, Bz.Kind.STRING_DELIMITER);
			definitions.put(symExclamation, Bz.Kind.RESERVED);
			definitions.put(symDoubleQuote, Bz.Kind.RESERVED);
			definitions.put(symPercent, Bz.Kind.RESERVED);
			definitions.put(symAmpersand, Bz.Kind.RESERVED);
			definitions.put(symSingleQuote, Bz.Kind.RESERVED);
			definitions.put(symColon, Bz.Kind.RESERVED);
			definitions.put(symQuestion, Bz.Kind.RESERVED);
			definitions.put(symAt, Bz.Kind.RESERVED);
			definitions.put(symBracketOpen, Bz.Kind.RESERVED);
			definitions.put(symBracketClose, Bz.Kind.RESERVED);
			definitions.put(symBackSlash, Bz.Kind.RESERVED);
			definitions.put(symPipe, Bz.Kind.RESERVED);
			definitions.put(symTilde, Bz.Kind.RESERVED);
			symbolDef = Map.copyOf(definitions); // create an immutable copy
		}
		
		public static final Map<String, Bz.Kind> wordDef;
		static{
			Map<String, Bz.Kind> definitions = new HashMap<String, Bz.Kind>();	
		
// Keywords
			definitions.put(kwLet, Bz.Kind.KW_LET);
			definitions.put(kwIf, Bz.Kind.KW_IF);
			definitions.put(kwElseIf, Bz.Kind.KW_ELSE_IF);
			definitions.put(kwElse, Bz.Kind.KW_ELSE);
			definitions.put(kwLoop, Bz.Kind.KW_LOOP);
			definitions.put(kwContinue, Bz.Kind.KW_CONTINUE);
			definitions.put(kwBreak, Bz.Kind.KW_BREAK);
// Functions
// input/output
			definitions.put(funPrint, Bz.Kind.LANG_FUN);
			definitions.put(funPrintLn, Bz.Kind.LANG_FUN);
			definitions.put(funPrintNum, Bz.Kind.LANG_FUN);
			definitions.put(funNewLine, Bz.Kind.LANG_FUN);
			definitions.put(funInputNumber, Bz.Kind.LANG_FUN);
			definitions.put(funInputString, Bz.Kind.LANG_FUN);
// string functions
			definitions.put(funLen, Bz.Kind.LANG_FUN);
			definitions.put(funStrCat, Bz.Kind.LANG_FUN);
			definitions.put(funMidStr, Bz.Kind.LANG_FUN);
			definitions.put(funStrComp, Bz.Kind.LANG_FUN);
			wordDef = Map.copyOf(definitions); // create an immutable copy
		}
	}
}
