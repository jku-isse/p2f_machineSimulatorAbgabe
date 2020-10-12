package at.pro2future.machineSimulator.methodService;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class OpcUaFileManager<M  extends JavaFileManager> extends ForwardingJavaFileManager<M> {

	private JavaFileObject outputJavaFileObject;
	
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
