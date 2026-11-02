package com.techport80.jlistcursor;

import java.util.*;
public class Cursor<T> {
	private final List<T> _items;
	private int _position;
	
	public Cursor(List<T> items) throws Exception {
		if (items == null) {
			throw new Exception("Constructor for Cursor does not allow a null ArrayList");
		}
		
		if (items.isEmpty() ) {
			throw new Exception("Constructor for Cursor does not allow an empty ArrayList");
		}
		_items = Collections.unmodifiableList(new ArrayList<>(items));
		_position = 0;
	}
	
	public T current(){
		return _items.get(_position);
	}
	
	public void rewind() {
		_position = 0;
	}
	
	public void fastForward() {
		_position = _items.size() - 1;
	}
	
	public boolean hasMore() {
		return _position < _items.size() - 1;
	}
	
	public boolean hasLess() {
		return _position > 0;
	}
	
	public T tryPeek() {
		if (hasMore()){
			return _items.get(_position + 1);
		}
		return null;
	}
	
	public T tryRecall() {
		if (hasLess()) {
			return _items.get(_position - 1);
		}
		return null;
	}
	
	public boolean back() {
		if (hasLess()) {
			_position--;
			return true;
		}
		return false;
	}
	
	public boolean next() {
		if (hasMore()) {
			_position++;
			return true;
		}
		return false;
	}
}
