package at.pro2future.machineSimulator;


/**
 * This exception will be thrown whenever a configuration is changed which cannot
 * be changed when the engine has already been started.
 *
 */
class AlreadyStartedException  extends Exception {


    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new {@link AlreadyStartedException}. This exception will be thrown whenever a configuration is changed which cannot
     * be changed when the engine has already been started.
     */
    AlreadyStartedException() {
        super();
    }
 
    /**
     * Creates a new {@link ConversionFailureException}. This exception will be thrown whenever a configuration is changed which cannot
     * be changed when the engine has already been started.s
     * 
     * @param message the message describing the cause of the exception {@link #getMessage()).
     */
    AlreadyStartedException(String message) {
        super(message);
    }

}
