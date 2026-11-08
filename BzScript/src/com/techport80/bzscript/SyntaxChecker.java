package com.techport80.bzscript;

import java.util.ArrayList;
import java.util.List;

import com.techport80.bzscript.Language.Bz;
import com.techport80.jlistcursor.*;

public class SyntaxChecker {
	
	Cursor<AstToken> cursor;
	public List<String> msg;
	
	public SyntaxChecker(List<AstToken> tokens) throws Exception {
		cursor = new Cursor<AstToken>(tokens);
		msg = new ArrayList<String>();
		
	}
	
	public List<String> getErrors() {
		return msg;
	}
	
	private boolean doAllowedOnlyLeft() {
	    AstToken c = cursor.current();

	    List<Bz.Kind> allowedKinds =
	        SyntaxRules.Rule.allowOnlyLeft.get(c.kind);

	    // If there are no rules, then we didn't break 'em.
	    if (allowedKinds == null) { return true; }

	    AstToken l = cursor.tryRecall();

	    if (l == null) {
	        if (allowedKinds.contains(Bz.Kind.START_OF_INPUT)) {
	            return true;
	        }

	        msg.add(
	            "Expected a token before [%s] on line [%d] column [%d]"
	                .formatted(c.name, c.lineNumber, c.columnNumber));

	        return false;
	    }

	    if (allowedKinds.contains(l.kind)) { return true; }

	    msg.add(
	        "Unexpected [%s] found before [%s] on line [%d] column [%d]"
	            .formatted(l.name, c.name, l.lineNumber, l.columnNumber));

	    return false;
	}

	private boolean doAllowedOnlyRight() {
	    AstToken c = cursor.current();

	    List<Bz.Kind> allowedKinds =
	        SyntaxRules.Rule.allowOnlyRight.get(c.kind);

	    // If there are no rules, then we didn't break 'em.
	    if (allowedKinds == null) { return true; }

	    AstToken r = cursor.tryPeek();

	    if (r == null) {
	        msg.add(
	            "Expected a token after [%s] on line [%d] column [%d]"
	                .formatted(c.name, c.lineNumber, c.columnNumber));

	        return false;
	    }

	    if (allowedKinds.contains(r.kind)) { return true; }

	    msg.add(
	        "Unexpected [%s] found after [%s] on line [%d] column [%d]"
	            .formatted(r.name, c.name, r.lineNumber, r.columnNumber));

	    return false;
	}
	
	private boolean doRejectAnyLeft() {
	    AstToken c = cursor.current();

	    List<Bz.Kind> rejectedKinds =
	        SyntaxRules.Rule.rejectAnyLeft.get(c.kind);

	    if (rejectedKinds == null) { return true; }

	    AstToken l = cursor.tryRecall();

	    if (l == null) {
	        if (!rejectedKinds.contains(Bz.Kind.START_OF_INPUT)) {
	            return true;
	        }

	        msg.add(
	            "Expected a token before [%s] on line [%d] column [%d]"
	                .formatted(c.name, c.lineNumber, c.columnNumber));

	        return false;
	    }

	    if (!rejectedKinds.contains(l.kind)) { return true; }

	    msg.add(
	        "Unexpected [%s] found before [%s] on line [%d] column [%d]"
	            .formatted(l.name, c.name, l.lineNumber, l.columnNumber));

	    return false;
	}

	private boolean doRejectAnyRight() {
	    AstToken c = cursor.current();

	    List<Bz.Kind> rejectedKinds =
	        SyntaxRules.Rule.rejectAnyRight.get(c.kind);

	    if (rejectedKinds == null) { return true; }

	    AstToken r = cursor.tryPeek();

	    if (r == null) { return true; }

	    if (!rejectedKinds.contains(r.kind)) { return true; }

	    msg.add(
	        "Unexpected [%s] found after [%s] on line [%d] column [%d]"
	            .formatted(r.name, c.name, r.lineNumber, r.columnNumber));

	    return false;
	}
	
	private boolean doBalancedGroups() {
				
		AstToken open = cursor.current();
		int nestLevel = 0;
		
		// the first significant token for this method is expected to be (
		// AstToken open should already be ( when the method is initially called.
		
		while (true) {
			AstToken c = cursor.current();
			if (c.kind == Bz.Kind.GROUP_OPEN) {
				nestLevel++;
				if (nestLevel == 1) {
					open = c;
				}
			}
			else if (c.kind == Bz.Kind.BLOCK_OPEN) {
				if (!doBalancedBlocks()){
					return false;
				}
			}
			else if(c.kind == Bz.Kind.BLOCK_CLOSE) {
				msg.add("Unexpected [%s] found on line [%d] column [%d]"
						.formatted(c.name, c.lineNumber, c.columnNumber));
				return false;
			}
			else if(c.kind == Bz.Kind.GROUP_CLOSE) {
				nestLevel--;
				if (nestLevel == 0){
					return true;
				}
			}
			
			if (!cursor.next()) {break;}
		}
			
		
		if (nestLevel != 0)	{
			msg.add("Could not find matched pair for [%s] found on line [%d] column [%d]"
					.formatted(open.name, open.lineNumber, open.columnNumber));
			return false;
		}
		
		return true;
	}
	
	private boolean doBalancedBlocks() {
		AstToken open = cursor.current();
		int nestLevel = 0;
		
		// the first significant token for this method is expected to be {
		// AstToken open should already be { when the method is initially called.
		
		while (true) {
			AstToken c = cursor.current();
			if (c.kind == Bz.Kind.BLOCK_OPEN) {
				nestLevel++;
				if (nestLevel == 1) {
					open = c;
				}
			}
			else if (c.kind == Bz.Kind.GROUP_OPEN) {
				if (!doBalancedGroups()){
					return false;
				}
			}
			else if(c.kind == Bz.Kind.GROUP_CLOSE) {
				msg.add("Unexpected [%s] found on line [%d] column [%d]"
						.formatted(c.name, c.lineNumber, c.columnNumber));
				return false;
			}
			else if(c.kind == Bz.Kind.BLOCK_CLOSE) {
				nestLevel--;
				if (nestLevel == 0){
					return true;
				}
			}
					
			
			if (!cursor.next()) {break;}
		}
			
		
		if (nestLevel != 0)	{
			msg.add("Could not find matched pair for [%s] found on line [%d] column [%d]"
					.formatted(open.name, open.lineNumber, open.columnNumber));
			return false;
		}
		
		return true;
	}
	
	public boolean check() {
		boolean retval = true;
		msg.clear();
		cursor.rewind();
		while (true) {
			if (!doAllowedOnlyLeft())   {retval = false;}
			if (!doAllowedOnlyRight()) {retval = false;}
			if (!doRejectAnyLeft())     {retval = false;}
			if (!doRejectAnyRight())    {retval = false;}
			if (!cursor.next()) {break;}
		}
		
		cursor.rewind();
		while(true) {
			AstToken c = cursor.current();
			if (c.kind == Bz.Kind.GROUP_OPEN) {
				if (!doBalancedGroups())   {retval = false;}
			}
			else if (c.kind == Bz.Kind.BLOCK_OPEN) {
				if (!doBalancedBlocks())   {retval = false;}
			}
			else if (c.kind == Bz.Kind.BLOCK_CLOSE  || 
					c.kind == Bz.Kind.GROUP_CLOSE) {
				retval = false;
				msg.add("Unexpected [%s] found on line [%d] column [%d]"
						.formatted(c.name, c.lineNumber, c.columnNumber));
			}
			
			if (!cursor.next()) {break;}
		}
		return retval;
	}
}
