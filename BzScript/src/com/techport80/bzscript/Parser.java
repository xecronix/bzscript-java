package com.techport80.bzscript;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.techport80.bzscript.Language.Bz;
import com.techport80.jlistcursor.Cursor;

public class Parser {
	
	Cursor<AstToken> cursor;
	private boolean groupsAlreadyParsed;
	enum ParseOrder {
		GROUP,
		FUNCTION, 
		EXPONENT, 
		UNARY,
		MULTIPLYDIVIDE,
		ADDSUBTRACT,
		BOOL,
		ASSIGNMENT,
		LET, 
		CONDITION_IF	
	};
	
	private List<Bz.Kind> boolKinds;
	private List<Bz.Kind> addKinds;
	private List<Bz.Kind> multiKinds;
	private List<Bz.Kind> unaryKinds;
	private List<Bz.Kind> exponentTerms;
		
	private Parser(List<AstToken> t) throws Exception{
		cursor = new Cursor<AstToken>(t);
		boolKinds = new ArrayList<Bz.Kind>();
		boolKinds.add(Bz.Kind.COMPARE_EQ);
		boolKinds.add(Bz.Kind.COMPARE_NOT_EQ);
		boolKinds.add(Bz.Kind.COMPARE_GREATER);
		boolKinds.add(Bz.Kind.COMPARE_GREATER_EQ);
		boolKinds.add(Bz.Kind.COMPARE_LESS);
		boolKinds.add(Bz.Kind.COMPARE_LESS_EQ);

		addKinds = new ArrayList<Bz.Kind>();
		addKinds.add(Bz.Kind.ADDITION);
		addKinds.add(Bz.Kind.SUBTRACTION);

		multiKinds = new ArrayList<Bz.Kind>();
		multiKinds.add(Bz.Kind.MULTIPLY);
		multiKinds.add(Bz.Kind.DIVIDE);
		
		unaryKinds = new ArrayList<Bz.Kind>();
		unaryKinds.add(Bz.Kind.UNARY_NEGATIVE);
		unaryKinds.add(Bz.Kind.UNARY_POSITIVE);
		
		exponentTerms = new ArrayList<Bz.Kind>();
		exponentTerms.add(Bz.Kind.LITERAL_NUMBER);
		exponentTerms.add(Bz.Kind.VAR_NUMBER);
		exponentTerms.add(Bz.Kind.LANG_FUN);
		exponentTerms.add(Bz.Kind.GROUP_OPEN);
		
		
	} 
	
	private void reduceBinaryOp(List<AstToken> target, List<Bz.Kind> kinds) throws Exception{
		
		while (true) {
			AstToken c = cursor.current();
			if (kinds.contains(c.kind)) {
				// makes sure there is a left side.
				if (target.isEmpty()) {
					throw new Exception ("Expected token before [%s] on line [%d] column [%d]"
							.formatted(c.name, c.lineNumber, c.columnNumber));
				}
				
				// makes sure there is a right side.
				if (!cursor.hasMore()) {
					throw new Exception ("Expected token after [%s] on line [%d] column [%d]"
							.formatted(c.name, c.lineNumber, c.columnNumber));
				}
				
				// we'll do the left side first
				c.tokens.add(tokOrGrp(target.removeLast()));
				
				// now the right side.
				c.tokens.add(tokOrGrp(cursor.tryPeek()));
				cursor.next(); // consume the peeked at token
	
			}
			target.add(c);
			if (!cursor.next()) {break;}
		}
	}
	
	private AstToken beginGroup(AstToken parent) throws Exception{
		
		List<AstToken> statement = new ArrayList<AstToken>();
		while (true){
			AstToken c = cursor.current();
			if (c.kind == Bz.Kind.GROUP_OPEN) {
				cursor.next();
				statement.add(beginGroup(c));
			}
			else if (c.kind == Bz.Kind.LIST_SEPARATOR){
				if (!statement.isEmpty()) {					
					Parser p = new Parser(statement);
					p.groupsAlreadyParsed = true;
					parent.tokens.add(p.parse());
				}

				statement.clear();
			}
			else if (c.kind == Bz.Kind.GROUP_CLOSE) {
				if (!statement.isEmpty()) {					
					Parser p = new Parser(statement);
					p.groupsAlreadyParsed = true;
					parent.tokens.add(p.parse());
				}

				return parent;
			}
			else {
				statement.add(c);
			}
			
			if (!cursor.next()) {break;} 
		}
		
		throw new Exception("Reached end of BzScript before closing group.");
	}
	
	private EnumSet<ParseOrder> doGroup(List<AstToken> target) throws Exception{
		EnumSet<ParseOrder> plan =  EnumSet.noneOf(ParseOrder.class);
		
		while (true) {
			AstToken c = cursor.current();
			if (c.kind == Bz.Kind.GROUP_OPEN && !groupsAlreadyParsed) {
				cursor.next();
				target.add(beginGroup(c));
				
			}
			else {
				// create execution plan
				if (c.kind == Bz.Kind.LANG_FUN) {
					plan.add(ParseOrder.FUNCTION);
				}
				else if (c.kind == Bz.Kind.EXPONENT) {
					plan.add(ParseOrder.EXPONENT);
				}
				else if (unaryKinds.contains(c.kind)){
					plan.add(ParseOrder.UNARY);
				}
				else if (multiKinds.contains(c.kind)){
					plan.add(ParseOrder.MULTIPLYDIVIDE);
				}
				else if (addKinds.contains(c.kind)){
					plan.add(ParseOrder.ADDSUBTRACT); 
				}
				else if (boolKinds.contains(c.kind) ) {
					plan.add(ParseOrder.BOOL);
				}
				else if (c.kind == Bz.Kind.ASSIGN) {
					plan.add(ParseOrder.ASSIGNMENT);
				}
				else if (c.kind == Bz.Kind.KW_LET) {
					plan.add(ParseOrder.LET);
				}
				else if (c.kind == Bz.Kind.KW_IF) {
					plan.add(ParseOrder.CONDITION_IF);
				}
				// add the current token to target.
				target.add(c);
			}
			
			 
			if (!cursor.next()) {break;}
		}
		
		return plan;
	}
	
	private void opFunction(List<AstToken> target) throws Exception{
		while (true) {
			AstToken c = cursor.current();
			if (c.kind == Bz.Kind.LANG_FUN){
				AstToken peek = cursor.tryPeek();
				boolean didPeek = peek != null;
				
				if (didPeek && peek.kind == Bz.Kind.GROUP_OPEN) {
					for (int i = 0; i < peek.tokens.size(); i++) {
						c.tokens.add(peek.tokens.get(i));
					}
				}
				else {
					throw new Exception("Function parameters not found after [%s] on line [%d] column [%d]"
							.formatted(c.name, c.lineNumber, c.columnNumber));
				}
				cursor.next(); // consume the peek
			}
			target.add(c);
			
			if (!cursor.next()) {break;}
		}
	}
	
	private AstToken tokOrGrp(AstToken t) throws Exception{
		if (t.kind == Bz.Kind.GROUP_OPEN) {
			if (t.tokens.isEmpty()) {
				throw new Exception ("Invalid empty group on line [%d] column [%d]"
						.formatted(t.lineNumber, t.columnNumber));
			}
			else if (t.tokens.size() != 1) {
					throw new Exception ("Unexpected group reduction on line [%d] column [%d]"
							.formatted(t.lineNumber, t.columnNumber));
			}
			
			return t.tokens.getFirst();
		}
		
		return t; 
	}
	
	private AstToken reduceExponentExpression(List<AstToken> expression) throws Exception{
		Cursor<AstToken> csr = new Cursor<AstToken>(expression);
		csr.fastForward();
		AstToken right = csr.current(); // for the linter.
		AstToken c = csr.current();     // for the linter.
		while (true) {
			c = csr.current();
			if (unaryKinds.contains(c.kind)) {
				c.tokens.add(tokOrGrp(right));
				right = c;
			}
			else if (c.kind == Bz.Kind.EXPONENT) {
				c.tokens.add(tokOrGrp(csr.tryRecall()));
				c.tokens.add(tokOrGrp(right));
				right = c;
				csr.back();
			}
			else {
				right = c;
			}
			
			if(!csr.back()) {break;}
		}
		
		return c;
	}
	

	private void opExponent(List<AstToken> target) throws Exception{
		List<AstToken> exponentExp = new ArrayList<AstToken>();
		
		while(true) {
			AstToken c = cursor.current();
			AstToken peek = cursor.tryPeek();
			boolean didPeek = peek != null;
			
			if (didPeek && exponentTerms.contains(c.kind) && peek.kind == Bz.Kind.EXPONENT) {
				exponentExp.clear();
				
				while(true) {
					c = cursor.current();
					peek = cursor.tryPeek();
					didPeek = peek != null;
					exponentExp.add(c); 
					
					if (!didPeek ||
				            !(exponentTerms.contains(peek.kind) ||
				              unaryKinds.contains(peek.kind) ||
				              peek.kind == Bz.Kind.EXPONENT)) {
				        break;
				    }

				    cursor.next();
				}
				
				target.add(reduceExponentExpression(exponentExp));
			}
			else {
				target.add(c);
			}
				
			if (!cursor.next()) {break;}
		}
		
	}
	
	private void opUnary(List<AstToken> target) throws Exception{
				
		while(true) {
			AstToken c = cursor.current();
			if(unaryKinds.contains(c.kind)) {
				AstToken parent = c;
				while (true) {
					AstToken peek = cursor.tryPeek();
					boolean didPeek = peek != null;
					
					if (!didPeek) {
						throw new Exception("Invalid unary [%s] found on line [%d] column [%d] "
								.formatted(c.name, c.lineNumber, c.columnNumber));
					}
					
					parent.tokens.add(peek);
					parent = peek;
					cursor.next();

					if (!unaryKinds.contains(peek.kind)) {
						break;
					}
						
				}
			}
			target.add(c);	
			if (!cursor.next()) {break;}
		}
	}
	
	private void opMultiplyDivide(List<AstToken> target) throws Exception{
		reduceBinaryOp(target, multiKinds);
	}
	
	private void opAddSubtract(List<AstToken> target) throws Exception{
		reduceBinaryOp(target, addKinds);
	}
	
	private void opBool(List<AstToken> target) throws Exception{
		reduceBinaryOp(target, boolKinds);
	}
	
	private void opAssignment(List<AstToken> target) throws Exception{
		List<Bz.Kind> kinds = new ArrayList<Bz.Kind>();
		kinds.add(Bz.Kind.ASSIGN);
		reduceBinaryOp(target, kinds);
	}
	
	private void opLet(List<AstToken> target) throws Exception{
		while (true) {
			AstToken c = cursor.current();
			if (c.kind == Bz.Kind.KW_LET){
				AstToken peek = cursor.tryPeek();
				boolean didPeek = peek != null;
				
				if (didPeek && peek.kind == Bz.Kind.ASSIGN) {
						c.tokens.add(peek);
				}
				else {
					throw new Exception("Expected assignment after [%s] on line [%d] column [%d]"
							.formatted(c.name, c.lineNumber, c.columnNumber));
				}
				cursor.next(); // consume the peek
			}
			target.add(c);
			
			if (!cursor.next()) {break;}
		}
	}
	
	private void opIf(List<AstToken> target) throws Exception{
		while (true) {
			AstToken c = cursor.current();
			if (c.kind == Bz.Kind.KW_IF || c.kind == Bz.Kind.KW_ELSE_IF ){
				AstToken peek = cursor.tryPeek();
				boolean didPeek = peek != null;
				
				if (didPeek && peek.kind == Bz.Kind.GROUP_OPEN) {
					if (peek.tokens.isEmpty()) {
						throw new Exception ("Found empty group after [%s] on line [%d] column [%d]"
								.formatted(c.name, c.lineNumber, c.columnNumber));
					}
					else if (peek.tokens.size() != 1) {
							throw new Exception ("Unexpected group reduction after [%s] on line [%d] column [%d]"
									.formatted(c.name, c.lineNumber, c.columnNumber));
					}
					c.tokens.add(peek); 
				}
				else {
					throw new Exception("Expected condition after [%s] on line [%d] column [%d]"
							.formatted(c.name, c.lineNumber, c.columnNumber));
				}
				cursor.next(); // consume the peek
			} 
			
			target.add(c);
			
			if (!cursor.next()) {break;}
		}
	}
	
	private AstToken parse() throws Exception{
		
		List<AstToken> buffer = new ArrayList<AstToken>();
		List<AstToken> backBuffer = new ArrayList<AstToken>();
		List<AstToken> target = backBuffer;
		boolean flipped = false;
		
		// doGroup will build the execution plan
		EnumSet<ParseOrder> plan = doGroup(target);
		
		// doGroup like all of the op functions
		// reduce tokens and puts the output in
		// target. We'll need to flip between 
		// backbuffer and buffer.  This avoids
		// new allocations of reduced lists. 
		for (ParseOrder p : plan) {

			// do the buffer swapping
			cursor = new Cursor<AstToken>(target);
			if (!flipped) {
				target = buffer;
				flipped = true;
			}
			else {
				target = backBuffer;
				flipped = false;
			}
			target.clear();
			
			// if/else if ops
			if (p == ParseOrder.FUNCTION) {
				opFunction(target);
			}
			else if (p == ParseOrder.EXPONENT) {
				opExponent(target);
			}
			else if (p == ParseOrder.UNARY) {
				opUnary(target);
			}
			else if (p == ParseOrder.MULTIPLYDIVIDE) {
				opMultiplyDivide(target);  
			}
			else if (p == ParseOrder.ADDSUBTRACT) {
				opAddSubtract(target);
			}
			else if (p == ParseOrder.BOOL) {
				opBool(target);
			}
			else if (p == ParseOrder.ASSIGNMENT) {
				opAssignment(target);
			}
			else if (p == ParseOrder.LET) {
				opLet(target);
			}
			else if (p == ParseOrder.CONDITION_IF) {
				opIf(target);
			}
		}
		
		if (target.size() != 1){
			throw new Exception ("Parse did not evaluate to a single token.");
		}
	
		return target.getFirst();
	}
	
	// creates a { AstToken
	// finds a statement ending with } or ;
	// if a { is found looking for statements recursively call begin
	// sends the statement to parse 
	// parse returns a single AstToken that needs to be added as a child of the initial {
	
	private AstToken begin(AstToken parent) throws Exception{
		// this method expects the cursor to be on the first token after
		// a block open or the first token in a new stream to parse.
		
		// I'm not sure if I need the root node info yet or not.  I 
		// feel like it might be helpful to detect a premature 
		// end of stream error.  We'll see.
		boolean rootNode = false;
		
		// create a node to attach statements to if not provided.
		if (parent == null) {
			parent = new AstToken();
			parent.kind = Bz.Kind.BLOCK_OPEN;
			rootNode = true;
		}
		
		List<AstToken> statement = new ArrayList<AstToken>();
		
		while (true) {
			// save a little typing and give us something 
			// to hold on to when we need to move one token
			// forward in the stream.  
			AstToken c = cursor.current();
			
			if (c.kind == Bz.Kind.BLOCK_OPEN) {
				cursor.next();
			    statement.add(begin(c));

			    Parser p = new Parser(statement);
			    parent.tokens.add(p.parse());
			    statement.clear();
			}
			
			else if (c.kind == Bz.Kind.BLOCK_CLOSE) {
				if (!statement.isEmpty()) {
					Parser p = new Parser(statement);
					parent.tokens.add(p.parse());
				}
				return parent;
			}
			
			else if (c.kind == Bz.Kind.STATEMENT_END) {
				// don't let some joker dev who thinks he's funny
				//  writing code like ;;;;;;;; rain on my parade.
				if (!statement.isEmpty()) {
					Parser p = new Parser(statement);
					parent.tokens.add(p.parse());
				}
				statement.clear();
			}
			else {
				statement.add(c);
			}
			
			if (!cursor.next()) {break;}
		}
		
		// hmm... this feels like I'm avoiding reporting an error.
		// I'll have to think about this more.  If I'm here and statement
		// isn't empty, that probably means there's a missing ;
		if (!statement.isEmpty()) {
			Parser p = new Parser(statement);
			parent.tokens.add(p.parse());
		}
		
		// If I'm here and this is not the root AST Node.  
		// there was a problem.  We'll throw an exception
		// and hope the stack trace gives us a clue.  
		
		if (!rootNode) {
			throw new Exception("Reached end of BzScript prematurely.");
		}
			
		return parent;
	}
	
	public static AstToken parse(List<AstToken> t) throws Exception{
		Parser p = new Parser(t);
		return p.begin(null);
	}
} 
