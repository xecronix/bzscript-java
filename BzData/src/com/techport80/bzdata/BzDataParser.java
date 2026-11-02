package com.techport80.bzdata;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import com.techport80.jlistcursor.*;

public class BzDataParser {
	
	private Reader reader;
	private BzData db;
	private List<Character> tokens;
	private Cursor<Character> cursor;
	private BzRecord record;
	private boolean isHeader = false;
	private boolean isFirstRow = true;
	private int maxFieldCount = 0;
	
	public BzDataParser(BzData db, Reader reader) {
		this.db = db;
		this.reader = reader;
		this.tokens = new ArrayList<Character>();
	}
	
	private BzField doField() throws Exception{
		String data = "";
		String comment = "";
		boolean isFieldComment = false;
		
		while (true) {
			char c = cursor.current();
			Character peek = cursor.tryPeek();
			boolean didPeek = peek != null;
			if (!isFieldComment && didPeek && c == '\\') {
			    if (peek == '|' || peek == '\\') {
			        data += peek;
			        cursor.next();
			    }
			    else {
			        data += c;
			    }
			}
			else if (c == '|'){
				if (isFieldComment == true) {
					return new BzField(data, comment);
				}
				else {
					isFieldComment = true;
				}
			}
			else if (c == ';' && isFieldComment) {
				return new BzField(data, comment);
			}
			else if(isFieldComment == true) {
				comment += c;
			}
			else {
				data += c;
			}
			
			if (!cursor.next()) {
				throw new Exception("Reached the end of a field and didnt' find a | or ;");
			}
		}
	}
	
	private void doRecordBegin() throws Exception{
		if (isHeader) {
			record = new BzRecord(db, BzRecord.Kind.HEADER);
		}
		else {
			record = new BzRecord(db, BzRecord.Kind.DATA);
		}
		
		BzField f;
		while (cursor.next()) {
			f = doField();
			if (f == null) {break;}
			record.addField(f);
			if (isHeader || isFirstRow) {
				maxFieldCount++;
			}
			
			if (cursor.current() == ';'){
				cursor.back();
				break;
			}
		}
		
		if (record.getFieldCount() != maxFieldCount) {
			throw new Exception("Found wrong number of fields");
		}
	}
	
	private void doComment() throws Exception{
		if (isFirstRow) {
			isHeader = true;
			while (cursor.next()){
				char c = cursor.current();
				if (c == '|') {
					cursor.back();
					break;
				}
				else if(Character.isWhitespace(c)) {
					continue;
				}
				
				throw new Exception("Leading characters after the header marker ! can only be whitespace.");
			}
			return;
		}
		else {
			record = new BzRecord(db, BzRecord.Kind.COMMENT);
			String comment = "";
			while (cursor.next()) {
				char c = cursor.current();
				if (c == ';') {
					BzField f = new BzField("", comment);
					record.addField(f);
					cursor.back();
					break;
				}
				else {
					comment += c;
				}
			}
		}
	}
	
	private void begin() throws Exception {
		cursor.rewind(); // shouldn't be needed but... JIC
		boolean isReading = false;
		while (true){
			char c = cursor.current();
			if (Character.isWhitespace(c)) {
				// skip the white space
			}
			else if (c == '!'){
				doComment();
				isReading = true;
			}
			else if (c == '|') {
				doRecordBegin();
				isReading = true;
			}
			else if (c == ';') {
				// doRecordEnd
				if (isHeader || isFirstRow) {
					if (record == null || record.getFieldCount() == 0) {
						throw new Exception("Headers and First Data Records must contain at least 1 BzField.");
					}
				}
				isHeader = false;
				isFirstRow = false;
				isReading = false;
				if (record != null) {
					db.addRecord(record);
					record = null;
				}
			}
			else {
				throw new Exception("Invalid character found %c".formatted(c));
			}
				
			if (!cursor.next()) {break;}
		}
		
		if (isReading == true) {
			throw new Exception("Reached the end of the stream without finding a terminating ;");
		}
	
	}
	
	public void parse() throws Exception{
		// first, let's get everything into a list chars
		int i;
		while ((i = reader.read()) != -1) {
			char c = (char)i;
			tokens.add(c);			
		}
		
		if (tokens.size()==0) {return;}
		
		cursor = new Cursor<Character>(tokens);
		begin();
	}

}
