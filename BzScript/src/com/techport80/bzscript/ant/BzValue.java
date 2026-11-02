package com.techport80.bzscript.ant;

public class BzValue {

	public static enum ValueKind{
		EMPTY,
		STRING, 
		DOUBLE
	};
	
	private String valString;
	private double valDouble;
	private ValueKind kind;
	
	public BzValue() {
		kind = ValueKind.EMPTY;
		valString = "";
		valDouble = 0.0;
	}
	
	public BzValue(double d) {
		kind = ValueKind.DOUBLE;
		valDouble = d;
	}
	
	public BzValue(String str) {
		kind = ValueKind.STRING;
		valString = str;
	}
	
	public String getString() throws Exception{
		if (kind !=ValueKind.EMPTY && kind ==ValueKind.STRING){
			return valString;
		}
		throw new BzValueException();
	}
	
	public double getDouble() throws Exception{
		if (kind !=ValueKind.EMPTY && kind ==ValueKind.DOUBLE){
			return valDouble;
		}
		throw new BzValueException();
	}
	
	public void setString(String str) throws Exception{
		if (kind ==ValueKind.EMPTY || kind == ValueKind.STRING){
			 valString = str;
			 kind = ValueKind.STRING;
			 return;
		}
		throw new BzValueException();
	}
	
	public void setDouble(double d) throws Exception{
		if (kind ==ValueKind.EMPTY || kind ==ValueKind.DOUBLE){
			valDouble =d;
			kind = ValueKind.DOUBLE;
			return;
		}
		throw new BzValueException();
	}
}
