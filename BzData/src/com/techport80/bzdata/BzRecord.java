package com.techport80.bzdata;

import java.util.ArrayList;
import java.util.List;

public class BzRecord {
	public static enum Kind{
		HEADER,
		FIRST, 
		COMMENT, 
		DATA
	};
	
	private BzData _parent;
	private Kind _kind;
	private List<BzField> _fields;
	private int _currentFieldCount;
	private ArrayList<String> _headers;
	
	private void init(BzData parent, Kind kind) {
		_fields = new ArrayList<BzField>();
		_kind = kind;
		_parent = parent;
		_headers = new ArrayList<String>();
		
	}
	
	public BzRecord (BzData parent, Kind kind) {
		init(parent, kind);
		//_maxFieldCount = requestFieldCount;  need to ask the parent for this info if not HEADER
	}
	
	public BzRow toRow() throws Exception{
		BzRow row = new BzRow();
		List<String> headers  = _parent.getHeaders();
		for (int i = 0; i < headers.size(); i++) {
			BzField f = _fields.get(i);
			row.put(headers.get(i), f.getData());
		}
		return row;
	}
		
	public int getFieldCount() {
		return _currentFieldCount;
	}
	
	public Kind getKind() {
		return _kind;
	}
	
	private boolean headerExists(String name) {
		for (int i = 0; i < _headers.size(); i++) {
			if (name.compareTo(_headers.get(i)) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public void addField(BzField f) throws Exception {
		if (this._kind == BzRecord.Kind.HEADER) {
			String newName = f.getData();
			newName = newName.trim();
			if (headerExists(newName)) {
				throw new Exception ("Cannot add header %s because it already exists.".formatted(newName));
			}
			else if (newName.length() == 0) {
				throw new Exception ("Cannot add header because it is an empty string.".formatted(newName));
			}
			else {
				_headers.add(newName);
			}
		}
		else {
			int maxFieldCount = _parent.getMaxFieldCount();
			// if maxFieldCount is 0 that means that the max hasn't been established yet.
			if (maxFieldCount > 0 && _currentFieldCount == maxFieldCount) {
				throw new Exception("Maximum number of fields already reached. Attempted to add [" + f.getData() + "]");
			}
		}
		
		_fields.add(f);
		_currentFieldCount++;
	}
	
	public BzField getField(String header) throws Exception{
		int fieldIndex = _parent.getHeaderIndex(header);
		
		if (fieldIndex == -1) {
			throw new Exception("Searched for a header named ["+header+"] but couldn't find one.");
		}
		
		if (fieldIndex >=_fields.size() || fieldIndex < 0) {
			throw new Exception("Header index for ["+header+"] out of bounds.");
		}
		
		return _fields.get(fieldIndex);
	}
	
	public BzField getField(int index) throws Exception{
		return _fields.get(index);
	}
	
	public String toString() {
		String retval = "";
		if (this._kind == BzRecord.Kind.HEADER || this._kind == BzRecord.Kind.COMMENT) {
			retval = "!";
		}
		
		if (this._kind == BzRecord.Kind.COMMENT && _fields.size() > 0) {
			retval += _fields.get(0).getComment();
			return retval  + ";\n";
		}
		
		for(int i=0; i<_currentFieldCount; i++) {
			retval += _fields.get(i).toString();
		}
		return retval + ";\n";
	}
}
