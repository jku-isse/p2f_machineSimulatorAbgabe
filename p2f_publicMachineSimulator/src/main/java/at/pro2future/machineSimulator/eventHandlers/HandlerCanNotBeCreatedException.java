package at.pro2future.machineSimulator.eventHandlers;

public class HandlerCanNotBeCreatedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public HandlerCanNotBeCreatedException() {
        super();
    }
 
    public HandlerCanNotBeCreatedException(String message) {
        super(message);
    }


    public HandlerCanNotBeCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerCanNotBeCreatedException(Throwable cause) {
        super(cause);
    }


    protected HandlerCanNotBeCreatedException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
