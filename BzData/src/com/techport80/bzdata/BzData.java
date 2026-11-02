package com.techport80.bzdata;

import com.techport80.jlistcursor.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.ArrayList;

public class BzData {
	
	private Reader reader;
	private Writer writer;
	
	private List<BzRecord> records;
	
	private void init() {
		records = new ArrayList<BzRecord>();
	}
	
	public BzData(Reader reader) {
		init();
		this.reader = reader;
	}
	
	public BzData(Reader reading, Writer writing) {
		init();
		this.reader = reading;
		this.writer = writing;
	}
	
	public void save() throws Exception{
		// This is probably a bad idea. If I thought this 
		// data were going to mega bytes or even giga bytes 
		// in size, I would use a different strategy.   
		// (like some sort of buffered writing)
		// but as it is, I'm not expecting that. 
		
		writer.write(this.toString());
	}
	
	public void open() throws Exception{
		BzDataParser p = new BzDataParser(this, this.reader);
		p.parse();
	}
	
	public int getMaxFieldCount()throws Exception{
		int retval = 0;
		if (records.size() > 0)
		{
			BzRecord head = records.getFirst();
			retval = head.getFieldCount();
		}
		return retval;
	}
	
	public List<String> getHeaders() throws Exception{
		List<String> retval = new ArrayList<String>();
		BzRecord head = records.getFirst();
		for(int i = 0; i < head.getFieldCount(); i++) {
			retval.add(head.getField(i).getData());
		}
		return retval;
	}
	
	public int getHeaderIndex(String name) throws Exception{
		int retval = -1;
		if (records.size() == 0) {
			return retval;
		}
		
		if (records.size() > 0)
		{
			BzRecord head = records.getFirst();
			if (head.getKind() != BzRecord.Kind.HEADER) {
				throw new Exception("First Record in Database was not a header");
			}
			for (int i = 0; i < head.getFieldCount(); i++) {
				BzField f = head.getField(i) ;
				if (f != null ) {
					name = name.trim();
					String headerName = f.getData().trim();
					if (name.compareTo(headerName)==0) {
						return i;
					}
				}
			}
		}
		return retval;
	}
	
	public void addRecord(BzRecord rec) {
		records.add(rec);
	}
	
	public String toString()
	{
		String retval = "";
		for(int i =0; i< records.size(); i++) {
			BzRecord r = records.get(i);
			retval += r.toString(); 
		}
		
		return retval;
	}
	
	public Cursor<BzRecord> getCursor() throws Exception{
		if (records.size() > 0){
			return new Cursor<BzRecord>(records);
		}
		return null;
	}
	
	public static void main() {
        try(
        	Writer writer = new FileWriter("testreader.bzdata");
			Reader reader = new FileReader("test.bzdata");
			
        ){
            // Use data here.
        	BzData data = new BzData(reader, writer);
        	data.open();
        	
        	System.out.println(data.toString());
        }
		catch (Exception ex) {
			ex.printStackTrace();
		}
    }
}
