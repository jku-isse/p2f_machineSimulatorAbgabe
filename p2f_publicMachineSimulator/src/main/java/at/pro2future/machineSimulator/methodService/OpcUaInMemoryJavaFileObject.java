package at.pro2future.machineSimulator.methodService;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * This class provides an in memory java file. The name, the content and the type can be provided
 * by using the constructor.
 */
public class OpcUaInMemoryJavaFileObject  extends SimpleJavaFileObject {
      final String content;

      /**
       * Creates a new in memory file identified by a file name and an extension. The content of this in memory file 
       * must be provided as well.
       * @param name the name of the file
       * @param kind provides the file extension of the java file.
       * @param content the content of the file.
       */
      public OpcUaInMemoryJavaFileObject(String name, Kind kind, String content) {
        super(URI.create("string:///" + name.replace('.','/') + kind.extension), kind);
        this.content = content;
      }
      
      /**
       * Returns the content of the in memory file.
       */
      @Override
      public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return this.content;
      }
}
