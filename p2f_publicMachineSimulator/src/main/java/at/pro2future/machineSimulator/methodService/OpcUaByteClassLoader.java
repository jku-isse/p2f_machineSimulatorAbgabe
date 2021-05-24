package at.pro2future.machineSimulator.methodService;

/**
 * This represents an <code>OpcUaByteClassLoader</code> which can load the 
 * classes represented by the given byte array..
 *  
 * @author johannstoebich
 *
 */
public class OpcUaByteClassLoader extends ClassLoader {


    byte[] byteArray;
    
    /**
     * Creates a class loader which can load the classes from the
     * binaries represented as byte array.
     * 
     * @param byteArray the byte array from which the classes should be loaded.
     */
    public OpcUaByteClassLoader(byte[] byteArray) {
        this(byteArray, null);
    }
    
    /**
     * Creates a class loader which can load the classes from the 
     * binaries represented as byte array and the additional classes proved by the parent <code>ClassLoader</code>.
     * 
     * @param byteArray the byte array from which the classes should be loaded.
     * @param parent the parent class loader from which the resolved classes should be loaded.
     */
    public OpcUaByteClassLoader(byte[] byteArray, ClassLoader parent) {
        super(parent);
        this.byteArray = byteArray;
    }

    /**
     * Returns the ByteArray provided with this adapter.
     * @return
     */
    public byte[] getByteArray() {
        return this.byteArray;
    }

    @Override
    public Class<?> findClass(String name) {
        return defineClass(name, this.byteArray, 0, this.byteArray.length);
    }
}
