package com.techport80.bzdata;

public class BzField {
	private String _comment;
	private String _data;
	
	private void init() {
		_comment = "";
		_data = "";
	}
	public BzField() {
		init();
	}
	
	public BzField(String data) {
		init();
		_data = data;
	}
	
	public BzField(String data, String comment) {
		init();
		_data = data;
		_comment = comment;
	}
	
	public String toString() {
	    String escaped = _data.replace("\\", "\\\\")
	                          .replace("|", "\\|");

	    return "|%s|%s".formatted(escaped, _comment);
	}
	
	public void setComment(String comment) { _comment = comment; }
	public void setData(String data) { _data = data; }
	
	public String getComment() {return _comment; }
	public String getData() { return _data; }
}
