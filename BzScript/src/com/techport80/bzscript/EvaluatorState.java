package com.techport80.bzscript;


import java.util.*;
import com.techport80.bzscript.ant.*;


public class EvaluatorState {

	List<Map<AstToken, RuntimeValues>> frameStack = new ArrayList<>();
	
	
	public int getFrameCount() {
		return frameStack.size();
	}
	
	public Map<AstToken, RuntimeValues> newFrame(AstToken parent){
		HashMap<AstToken, RuntimeValues> hm = new HashMap<AstToken, RuntimeValues>();
		hm.put(parent, new RuntimeValues());
		frameStack.add(hm);
		return hm;
	}
	
	public Map<AstToken, RuntimeValues> popFrame(){
		return frameStack.removeLast(); 
	}
	
	public Map<AstToken, RuntimeValues> getFrame(int i){
		return frameStack.get(i); 
	}
	
	public class RuntimeValues{
		public Map<String, BzValue> data = new HashMap<String, BzValue>();
	}
	
}
 