package at.pro2future.machineSimulator.methodService;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

/**
 * This file manager retrieves always the same {@link JavaFileObject}. This 
 * object can be handed in by using constructor.
 * 
 */
final class OpcUaFileManager<M  extends JavaFileManager> extends ForwardingJavaFileManager<M> {

    private JavaFileObject outputJavaFileObject;
    
    /**
     * Creates a new file manager, that will return the given {@link JavaFileObject} on invocation.
     * 
     * @param fileManager the file manager which is required to load the file.
     * @param outputJavaFileObject the object which will be returned by the file manager.
     */
    protected OpcUaFileManager(M fileManager, JavaFileObject outputJavaFileObject) {
        super(fileManager);
        this.outputJavaFileObject = outputJavaFileObject;
    }
    
     
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
            throws IOException {
        return this.outputJavaFileObject;
    }

}
