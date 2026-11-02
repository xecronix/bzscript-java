package com.techport80.bzscript.ant;

import com.techport80.bzscript.AstToken;

public interface IAnt {
	public EvalMsg exe(AstToken t) throws Exception;  
}
