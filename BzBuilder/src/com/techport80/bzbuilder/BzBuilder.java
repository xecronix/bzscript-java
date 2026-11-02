package com.techport80.bzbuilder;

public class BzBuilder {
	
	public static void main() {
		System.out.println("Building Managed Classes for BzScript");
		
		// one day, this might be command line args. Maybe if I port BzScript 
		// to another language. For now though, these are the base paths.
		
		try {
		
			LanguageBuilder langBuilder = new LanguageBuilder("templates/java", "../BzScript/", "../BzScript/src/com/techport80/bzscript");
			langBuilder.build();
			
			SyntaxRulesBuilder synBuilder = new SyntaxRulesBuilder("templates/java", "../BzScript/", "../BzScript/src/com/techport80/bzscript");
			synBuilder.build();
			
			EvaluatorBuilder evaluatorBuilder = new EvaluatorBuilder("templates/java", "../BzScript/", "../BzScript/src/com/techport80/bzscript");
			evaluatorBuilder.build();
			
			AntStubBuilder antStubBuilder = new AntStubBuilder("templates/java", "../BzScript/", "../BzScript/src/com/techport80/bzscript/ant");
			antStubBuilder.build();
			
			LangFunBuilder langFunBuilder = new LangFunBuilder("templates/java", "../BzScript/", "../BzScript/src/com/techport80/bzscript/ant");
			langFunBuilder.build();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();			
		}
		
	}
}
