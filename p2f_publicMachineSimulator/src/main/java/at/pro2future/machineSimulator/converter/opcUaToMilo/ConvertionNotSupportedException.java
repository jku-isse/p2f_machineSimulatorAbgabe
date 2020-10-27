package at.pro2future.machineSimulator.converter.opcUaToMilo;

public class ConvertionNotSupportedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConvertionNotSupportedException() {
        super();
    }
 
    public ConvertionNotSupportedException(String message) {
        super(message);
    }


    public ConvertionNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertionNotSupportedException(Throwable cause) {
        super(cause);
    }


    protected ConvertionNotSupportedException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
