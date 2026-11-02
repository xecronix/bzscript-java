package com.techport80.bzscript;
public class RunTests {
 public static void main(String[] args) throws Exception {
 int passed=0, failed=0;
 for (var m: SyntaxCheckerTests.class.getDeclaredMethods()) {
 if (!m.isAnnotationPresent(org.junit.jupiter.api.Test.class)) continue;
 try {m.invoke(new SyntaxCheckerTests()); passed++;}
 catch (java.lang.reflect.InvocationTargetException e) {failed++; System.out.println(m.getName()+": "+e.getCause());}
 }
 System.out.println("Passed: "+passed+", failed: "+failed);
 if(failed>0) System.exit(1);
 }
}
