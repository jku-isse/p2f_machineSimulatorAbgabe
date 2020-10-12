package at.pro2future.machineSimulator.methodService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class OpcUaByteJavaFileObject  extends SimpleJavaFileObject {
	  final ByteArrayOutputStream byteArrayOutputStream;
	
	  public OpcUaByteJavaFileObject(String name) {
		  super(URI.create("bytes:///"+name + name.replaceAll("\\.", "/")), Kind.CLASS);
		  byteArrayOutputStream = new ByteArrayOutputStream();
	  }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return byteArrayOutputStream;
    }
    
    public byte[] getBytes() {
        return byteArrayOutputStream.toByteArray();
    }
}
