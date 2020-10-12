package at.pro2future.machineSimulator.methodService;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class OpcUaStringJavaFileObject  extends SimpleJavaFileObject {
	  final String code;

	  public OpcUaStringJavaFileObject(String name, String code) {
	    super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),Kind.SOURCE);
	    this.code = code;
	  }

	  @Override
	  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
	    return code;
	  }
}
