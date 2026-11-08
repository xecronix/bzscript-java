package com.techport80.bzscript;

import java.util.HashMap;
import java.util.Map;

import com.techport80.bzscript.ant.BzValue;

public class RuntimeFrame{
	public AstToken token;
	public Map<String, BzValue> values = new HashMap<>();
	
	public RuntimeFrame(AstToken t) {
		token = t;
	}
}