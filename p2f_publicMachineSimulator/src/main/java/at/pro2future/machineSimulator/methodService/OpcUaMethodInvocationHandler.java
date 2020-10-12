package at.pro2future.machineSimulator.methodService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.eclipse.milo.opcua.sdk.client.methods.UaMethodException;
import org.eclipse.milo.opcua.sdk.server.api.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

import OpcUaDefinition.MsMethodNode;

/*From https://www.logicbig.com/tutorials/core-java-tutorial/java-se-compiler-api/compiler-api-memory-loader.html*/
public class OpcUaMethodInvocationHandler extends AbstractMethodInvocationHandler{

	private static final String CLASS_NAME = "WrapperClass";
	private byte [] compiledProgram;
	private MsMethodNode msNode;

	
	public OpcUaMethodInvocationHandler(UaMethodNode uaMethodNode, MsMethodNode msNode) {
		super(uaMethodNode);
		this.msNode = msNode;
		compiledProgram = compile(this.msNode.getMethod());
	}

	private byte[] compile(String method) {
		//setup comiler and diagnostics
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        
        //setup input file
        String classToCompile = "import org.eclipse.milo.opcua.stack.core.types.builtin.Variant; "+
        		 "public class " + CLASS_NAME + " { " + method + " }";
        OpcUaStringJavaFileObject stringObject =   new OpcUaStringJavaFileObject(CLASS_NAME, classToCompile);
        Iterable<? extends JavaFileObject> inputFiles = Arrays.asList(stringObject);
        
        //setup output file
        final OpcUaByteJavaFileObject outputFile = new OpcUaByteJavaFileObject(CLASS_NAME);
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileManager outputFileManager = new OpcUaFileManager<StandardJavaFileManager>(standardFileManager, outputFile);

        //compile
        CompilationTask task = compiler.getTask(null, outputFileManager, diagnostics, null, null, inputFiles);
        if (!task.call()) {
            diagnostics.getDiagnostics().forEach(System.out::println);
        }
        
        return outputFile.getBytes();
	}

	@Override
	public Argument[] getInputArguments() {
		return getNode().getInputArguments();
	}

	@Override
	public Argument[] getOutputArguments() {
		return getNode().getOutputArguments();
	}

	@Override
	public Variant[] invoke(InvocationContext invocationContext, Variant[] inputValues) throws UaException {
		OpcUaByteClassLoader classLoader = new OpcUaByteClassLoader(compiledProgram,  ClassLoader.getSystemClassLoader());
		
		try {
			Class<?> wrapperClass = Class.forName(CLASS_NAME, false, classLoader);
			
			if(wrapperClass.getMethods().length < 1) {
				throwUaException("Compiled method not found.");
			}
			
			Method methodToInvoke = wrapperClass.getMethods()[0];
			if(methodToInvoke.getParameterTypes().length != 1) {
				throwUaException("Wrong amout of parameters.");
			}
			else if	(methodToInvoke.getParameterTypes()[0] != inputValues.getClass()) {
				throwUaException("Wrong input paramter type.");
			}
			else if	(methodToInvoke.getReturnType() != inputValues.getClass()) {
				throwUaException("Wrong return paramter type.");
			}
			
			Object wrapperClassInstance = wrapperClass.newInstance();
			Object result = methodToInvoke.invoke(wrapperClassInstance, new Object[] {inputValues});
			
			return (Variant[])result;
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | InstantiationException e) {
			throwUaException(e.getMessage());
		}
		return null;
	}
	
	
	private void throwUaException(String errorText) throws UaMethodException {
		DiagnosticInfo[] diagnosticInfos = new DiagnosticInfo[1];
		diagnosticInfos[0]  = new DiagnosticInfo(-1, -1, -1, -1, errorText, null, null);
		throw new UaMethodException(StatusCode.BAD, null, diagnosticInfos);
	}
	
}
