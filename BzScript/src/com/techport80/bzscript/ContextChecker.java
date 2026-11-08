package com.techport80.bzscript;

import java.util.ArrayList;
import java.util.List;

import com.techport80.bzscript.Language.Bz;

public class ContextChecker {

	AstToken root;
	List<AstToken> containerStack;
	public ContextChecker(AstToken root) {
		this.root = root;
		containerStack = new ArrayList<AstToken>();
	}
	
	private boolean findValid(Bz.Kind needle, Bz.Kind endSentinel) {
		boolean retval = false;
		
		int top = containerStack.size();
		
		while (top != 0)
		{
			top--;
			AstToken t = containerStack.get(top);
			if (t.kind == needle) {
				return true;
			}
			else if(t.kind == endSentinel) {
				return false;
			}
		}
		
		return retval;
	}
	
	private boolean doBlock(AstToken container, AstToken block) throws Exception{
		boolean retval = true;
		boolean ifSequence = false;
		try {
			containerStack.add(container);
			for (AstToken child : block.tokens) {
				
				if(KindGroups.getConditionalBlocks().contains(child.kind)) {
					if (child.kind == Bz.Kind.KW_ELSE_IF && !ifSequence) {
						throw new Exception ("Invalid [%s] found on line [%d] column [%d]"
								.formatted(child.name, child.lineNumber, child.columnNumber));
					}
					ifSequence = true;
					retval = doBlock(child, child.tokens.get(1));
				}
				else if(KindGroups.getUnconditionalBlocks().contains(child.kind)) {
					if (child.kind == Bz.Kind.KW_ELSE && !ifSequence) {
						throw new Exception ("Invalid [%s] found on line [%d] column [%d]"
								.formatted(child.name, child.lineNumber, child.columnNumber));
					}

					ifSequence = false;
					retval = doBlock(child, child.tokens.get(0));
				}
				else if (child.kind == Bz.Kind.KW_BREAK || child.kind == Bz.Kind.KW_CONTINUE) {
					ifSequence = false;
					if (!findValid(Bz.Kind.KW_LOOP, Bz.Kind.RESERVED)){  // reserved is here for now.  This should be USER_FUN later.
						throw new Exception ("Invaldi [%s] found on line [%d] column [%d]"
								.formatted(child.name, child.lineNumber, child.columnNumber));
					}
				}
				else if(child.kind == Bz.Kind.BLOCK_OPEN) {
					retval = doBlock(child, child);	// ATM setting retval is meaningless.  Maybe later it will be important. 			
				}
				else {
					ifSequence = false; 
				}
			}
		}
		finally {
			containerStack.removeLast();
		}
		
		
		return retval;
	}
	
	public boolean check() throws Exception{
		return doBlock(root, root);
	}
}
