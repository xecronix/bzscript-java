package com.techport80.bzscript;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techport80.bzscript.Language.Bz;

public class AstToken {
	public String name;
	public List<AstToken> tokens;
	public Bz.Kind kind;
	public int lineNumber;
	public int columnNumber;
	public String fileName;
	
	public AstToken(String name, String fileName, int lineNumber, int columnNumber, Bz.Kind kind) {
		this.name = name;
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.kind = kind;
		this.tokens = new ArrayList<AstToken>();	
	}
	
	public AstToken() {
		this.name = "";
		this.fileName = "";
		this.lineNumber = 0;
		this.columnNumber = 0;
		this.kind = Bz.Kind.UNKNOWN;
		this.tokens = new ArrayList<AstToken>();	
	}
	
	private static final Gson JSON = new GsonBuilder()
	        .setPrettyPrinting()
	        .create();

	public String toJSON() {
	    return JSON.toJson(this);
	}
	
	public void serialize(boolean printHeader, StringBuilder sb) {
		if (printHeader == true) {
			sb.append("!|name| |fileName| |lineNumber| |columnNumber| |kind| |tokenCount|;\n");
		}
		int tokenCount = tokens.size();
		sb.append("|%s| |%s| |%s| |%s| |%s| |%d|;\n"
				.formatted(name, fileName, lineNumber, columnNumber, kind.toString(), tokenCount));
		
		for(AstToken child : tokens) {
			child.serialize(false, sb);
		}
	}
}
