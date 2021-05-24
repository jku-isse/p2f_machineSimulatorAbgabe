package at.pro2future.machineSimulator.converter;


/**
 * This exception will be thrown when a convention from an machine simulator object to 
 * an OPC-UA object is currently not supported.
 * <p> 
 * It might be supported in future versions. 
 *
 */
public class ConvertionNotSupportedException extends Exception {


    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new {@link ConvertionNotSupported}. This exception will be thrown 
     * when a conversions from an machine simulator object to an OPC-UA object is not supported.
     */
    public ConvertionNotSupportedException() {
        super();
    }
 
    /**
     * Creates a new {@link ConvertionNotSupported}. This exception will be thrown 
     * when a conversions from an machine simulator object to an OPC-UA object is not supported.
     * 
     * @param message the message describing the cause of the exception {@link #getMessage()).
     */
    public ConvertionNotSupportedException(String message) {
        super(message);
    }
}
