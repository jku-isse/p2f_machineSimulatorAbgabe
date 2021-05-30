package at.pro2future.machineSimulator.methodService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * This class represents an in memory file. It stores the content 
 * of the file as an {@link ByteArrayOutputStream}.
 *
 */
class OpcUaByteJavaFileObject  extends SimpleJavaFileObject {
    final ByteArrayOutputStream byteArrayOutputStream;
    
    /**
     * This class represents an in memory file. It stores the content 
     * of the file as an {@link ByteArrayOutputStream}.
     * @param name the name of the in memory file.
     */
    OpcUaByteJavaFileObject(String name) {
        super(URI.create("bytes:///"+name + name.replaceAll("\\.", "/")), Kind.CLASS);
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return this.byteArrayOutputStream;
    }
    
    byte[] getBytes() {
        return this.byteArrayOutputStream.toByteArray();
    }
}
