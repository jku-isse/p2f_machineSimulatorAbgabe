package at.pro2future.machineSimulator.converter;


/**
 * This exception will be thrown when a convention error occurs.
 *  
 * @author johannstoebich
 *
 */
public class ConversionFailureException extends Exception {


    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new {@link ConversionFailureException}. This exception will be thrown 
     * when a conversions from an machine simulator object to an OPC-UA object fails.
     */
    public ConversionFailureException() {
        super();
    }
 
    /**
     * Creates a new {@link ConversionFailureException}. This exception will be thrown 
     * when a conversions from an machine simulator object to an OPC-UA object fails.
     * 
     * @param message the message describing the cause of the exception {@link #getMessage()).
     */
    public ConversionFailureException(String message) {
        super(message);
    }


    /**
     * Create a new {@link ConversionFailureException}. This exception will be thrown
     * when a conversions from an machine simulator object to an OPC-UA object fails.
     * 
     * @param message the message describing the cause of the exception {@link #getMessage()).
     * @param cause the reason why the exception has been thrown {@link #getCause()}.
     */
    public ConversionFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
