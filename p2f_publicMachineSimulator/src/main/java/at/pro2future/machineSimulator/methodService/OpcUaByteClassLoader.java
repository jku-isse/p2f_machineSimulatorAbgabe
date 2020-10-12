package at.pro2future.machineSimulator.methodService;

/**
 * Form https://stackoverflow.com/questions/7989135/is-it-possible-to-programmatically-compile-java-source-code-in-memory-only
 * @author johannstoebich
 *
 */
public class OpcUaByteClassLoader extends ClassLoader {

	byte[] byteArray;
	
	public OpcUaByteClassLoader(byte[] byteArray) {
		this(byteArray, null);
	}
	
	public OpcUaByteClassLoader(byte[] byteArray, ClassLoader parrent) {
		super(parrent);
		this.byteArray = byteArray;
	}
	
	public byte[] getByteArray() {
		return byteArray;
	}
	
    public Class<?> findClass(String name) {
        return defineClass(name, byteArray, 0, byteArray.length);
    }
}
