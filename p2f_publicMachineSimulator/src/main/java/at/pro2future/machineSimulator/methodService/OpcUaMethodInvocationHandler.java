package at.pro2future.machineSimulator.methodService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;
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

/**
 * This <code>OpcUaMethodInvocationHandler</code> can compile java code which
 * is provided as a {@link MsMethodNode} in a first step an then can invoke
 * methods on the compiled code in a second step. The code will be compiled during the 
 * creation of this class.
 *
 */
/*From https://www.logicbig.com/tutorials/core-java-tutorial/java-se-compiler-api/compiler-api-memory-loader.html*/
public class OpcUaMethodInvocationHandler extends AbstractMethodInvocationHandler {

    private static final String CLASS_NAME = "WrapperClass";
    private byte [] compiledProgram;
    private MsMethodNode msNode;

    
    /**
     * Creates a new <code>OpcUaMethodInvocationHandler</code>. During construction the code will be compiled.
     * The code which should be compiled is provides by the given {@link MsMethodNode}.
     * @param uaMethodNode the handler which contains the called method.
     * @param msNode the configuration which also contains the method code which should be compiled.
     * @throws IOException
     */
    public OpcUaMethodInvocationHandler(UaMethodNode uaMethodNode, MsMethodNode msNode) throws IOException {
        super(uaMethodNode);
        this.msNode = msNode;
        this.compiledProgram = compile(this.msNode.getMethod());
    }

    private static byte[] compile(String method) throws IOException {
        //setup comiler and diagnostics
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        
        //setup input file
        String classToCompile = "import org.eclipse.milo.opcua.stack.core.types.builtin.Variant; "+
                 "public class " + CLASS_NAME + " { " + method + " }";
        OpcUaInMemoryJavaFileObject stringObject =   new OpcUaInMemoryJavaFileObject(CLASS_NAME, Kind.SOURCE, classToCompile);
        Iterable<? extends JavaFileObject> inputFiles = Arrays.asList(stringObject);
        
        //setup output file
        final OpcUaByteJavaFileObject outputFile = new OpcUaByteJavaFileObject(CLASS_NAME);
        byte[] bytes;
        
        try(StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null)){
            try(JavaFileManager outputFileManager = new OpcUaFileManager<>(standardFileManager, outputFile)){
                //compile
                CompilationTask task = compiler.getTask(null, outputFileManager, diagnostics, null, null, inputFiles);
                if (!task.call().booleanValue()) {
                    diagnostics.getDiagnostics().forEach(System.out::println);
                }
                
                bytes = outputFile.getBytes();
            }
            
        }
        
        return bytes;
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
        OpcUaByteClassLoader classLoader = new OpcUaByteClassLoader(this.compiledProgram,  ClassLoader.getSystemClassLoader());
        
        try {
            Class<?> wrapperClass = Class.forName(CLASS_NAME, false, classLoader);
            
            if(wrapperClass.getMethods().length < 1) {
                throwUaException("Compiled method not found.");
            }
            
            Method methodToInvoke = wrapperClass.getMethods()[0];
            if(methodToInvoke.getParameterTypes().length != 1) {
                throwUaException("Wrong amout of parameters.");
            }
            else if    (methodToInvoke.getParameterTypes()[0] != inputValues.getClass()) {
                throwUaException("Wrong input paramter type.");
            }
            else if    (methodToInvoke.getReturnType() != inputValues.getClass()) {
                throwUaException("Wrong return paramter type.");
            }
            
            Object wrapperClassInstance = wrapperClass.getDeclaredConstructors()[0].newInstance();
            Object result = methodToInvoke.invoke(wrapperClassInstance, new Object[] {inputValues});
            
            return (Variant[])result;
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | InstantiationException e) {
            throwUaException(e.getMessage());
        }
        return null;
    }
    
    
    private static void throwUaException(String errorText) throws UaMethodException {
        DiagnosticInfo[] diagnosticInfos = new DiagnosticInfo[1];
        diagnosticInfos[0]  = new DiagnosticInfo(-1, -1, -1, -1, errorText, null, null);
        throw new UaMethodException(StatusCode.BAD, null, diagnosticInfos);
    }


    
}
