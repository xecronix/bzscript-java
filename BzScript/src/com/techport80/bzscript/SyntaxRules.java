package com.techport80.bzscript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techport80.bzscript.Language.Bz;

public class SyntaxRules {
    public static class Rule {
        
        public static final Map<Bz.Kind, List<Bz.Kind>> rejectAnyLeft
            = new HashMap<>();

        static {
            
        }
        
        public static final Map<Bz.Kind, List<Bz.Kind>> rejectAnyRight
            = new HashMap<>();

        static {
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_STRING);
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.VAR_STRING);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                rejectAnyRight.put(Bz.Kind.GROUP_CLOSE, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_STRING);
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.VAR_STRING);
                
                rejectAnyRight.put(Bz.Kind.VAR_NUMBER, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_STRING);
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.VAR_STRING);
                
                rejectAnyRight.put(Bz.Kind.VAR_STRING, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_STRING);
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.VAR_STRING);
                
                rejectAnyRight.put(Bz.Kind.LITERAL_NUMBER, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_STRING);
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.VAR_STRING);
                
                rejectAnyRight.put(Bz.Kind.LITERAL_STRING, ls);
            }
            
        }
        
        public static final Map<Bz.Kind, List<Bz.Kind>> allowOnlyLeft
            = new HashMap<>();

        static {
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.STATEMENT_END);
                
                ls.add(Bz.Kind.BLOCK_OPEN);
                
                ls.add(Bz.Kind.BLOCK_CLOSE);
                
                ls.add(Bz.Kind.START_OF_INPUT);
                
                allowOnlyLeft.put(Bz.Kind.KW_LET, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.STATEMENT_END);
                
                ls.add(Bz.Kind.BLOCK_OPEN);
                
                ls.add(Bz.Kind.BLOCK_CLOSE);
                
                ls.add(Bz.Kind.START_OF_INPUT);
                
                allowOnlyLeft.put(Bz.Kind.KW_LOOP, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.STATEMENT_END);
                
                ls.add(Bz.Kind.BLOCK_OPEN);
                
                ls.add(Bz.Kind.BLOCK_CLOSE);
                
                ls.add(Bz.Kind.START_OF_INPUT);
                
                allowOnlyLeft.put(Bz.Kind.KW_BREAK, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.STATEMENT_END);
                
                ls.add(Bz.Kind.BLOCK_OPEN);
                
                ls.add(Bz.Kind.BLOCK_CLOSE);
                
                ls.add(Bz.Kind.START_OF_INPUT);
                
                allowOnlyLeft.put(Bz.Kind.KW_CONTINUE, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.STATEMENT_END);
                
                ls.add(Bz.Kind.BLOCK_OPEN);
                
                ls.add(Bz.Kind.BLOCK_CLOSE);
                
                ls.add(Bz.Kind.START_OF_INPUT);
                
                allowOnlyLeft.put(Bz.Kind.KW_ELSE, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.STATEMENT_END);
                
                ls.add(Bz.Kind.BLOCK_OPEN);
                
                ls.add(Bz.Kind.BLOCK_CLOSE);
                
                ls.add(Bz.Kind.START_OF_INPUT);
                
                allowOnlyLeft.put(Bz.Kind.KW_ELSE_IF, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.STATEMENT_END);
                
                ls.add(Bz.Kind.BLOCK_OPEN);
                
                ls.add(Bz.Kind.BLOCK_CLOSE);
                
                ls.add(Bz.Kind.START_OF_INPUT);
                
                allowOnlyLeft.put(Bz.Kind.KW_IF, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.ADDITION, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.SUBTRACTION, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.MULTIPLY, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.DIVIDE, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.EXPONENT, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.COMPARE_EQ, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.COMPARE_GREATER, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.COMPARE_LESS, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.COMPARE_GREATER_EQ, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.COMPARE_LESS_EQ, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.COMPARE_NOT_EQ, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.VAR_STRING);
                
                allowOnlyLeft.put(Bz.Kind.ASSIGN, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.STATEMENT_END);
                
                ls.add(Bz.Kind.BLOCK_OPEN);
                
                ls.add(Bz.Kind.BLOCK_CLOSE);
                
                allowOnlyLeft.put(Bz.Kind.BLOCK_CLOSE, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                ls.add(Bz.Kind.KW_ELSE);
                
                ls.add(Bz.Kind.KW_LOOP);
                
                allowOnlyLeft.put(Bz.Kind.BLOCK_OPEN, ls);
            }
            
        }
        
        public static final Map<Bz.Kind, List<Bz.Kind>> allowOnlyRight
            = new HashMap<>();

        static {
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                allowOnlyRight.put(Bz.Kind.KW_IF, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                allowOnlyRight.put(Bz.Kind.KW_ELSE_IF, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.BLOCK_OPEN);
                
                allowOnlyRight.put(Bz.Kind.KW_ELSE, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.BLOCK_OPEN);
                
                allowOnlyRight.put(Bz.Kind.KW_LOOP, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.VAR_STRING);
                
                allowOnlyRight.put(Bz.Kind.KW_LET, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                allowOnlyRight.put(Bz.Kind.LANG_FUN, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.STATEMENT_END);
                
                allowOnlyRight.put(Bz.Kind.KW_BREAK, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.STATEMENT_END);
                
                allowOnlyRight.put(Bz.Kind.KW_CONTINUE, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.ADDITION, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.SUBTRACTION, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.MULTIPLY, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.DIVIDE, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.EXPONENT, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.COMPARE_EQ, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.COMPARE_GREATER, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.COMPARE_LESS, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.COMPARE_GREATER_EQ, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.COMPARE_LESS_EQ, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.COMPARE_NOT_EQ, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_STRING);
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.VAR_STRING);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.LANG_FUN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.ASSIGN, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.UNARY_NEGATIVE, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.UNARY_POSITIVE, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_STRING);
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.VAR_STRING);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.LANG_FUN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.LIST_SEPARATOR, ls);
            }
            
            {
                List<Bz.Kind> ls = new ArrayList<>();
                
                ls.add(Bz.Kind.LITERAL_STRING);
                
                ls.add(Bz.Kind.LITERAL_NUMBER);
                
                ls.add(Bz.Kind.VAR_NUMBER);
                
                ls.add(Bz.Kind.VAR_STRING);
                
                ls.add(Bz.Kind.GROUP_OPEN);
                
                ls.add(Bz.Kind.GROUP_CLOSE);
                
                ls.add(Bz.Kind.LANG_FUN);
                
                ls.add(Bz.Kind.UNARY_NEGATIVE);
                
                ls.add(Bz.Kind.UNARY_POSITIVE);
                
                allowOnlyRight.put(Bz.Kind.GROUP_OPEN, ls);
            }
            
        }
        
    }
}