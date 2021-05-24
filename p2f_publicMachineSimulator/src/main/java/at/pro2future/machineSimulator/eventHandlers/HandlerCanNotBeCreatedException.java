package at.pro2future.machineSimulator.eventHandlers;

/**
 * This exception is thrown when a {@link CallMethodHandler}, {@link ReadVariablesHandler} or {@link WriteVariablesHandler} cannot be instantiated.
 *
 *
 */
public class HandlerCanNotBeCreatedException extends Exception {

    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new <code>HandlerCanNotBeCreatedException</code> with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public HandlerCanNotBeCreatedException() {
        super();
    }

    /**
     * Constructs a new <code>HandlerCanNotBeCreatedException</code> with the specified detail message. 
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public HandlerCanNotBeCreatedException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of {@code (cause==null ? null : cause.toString())} (which
     * typically contains the class and detail message of {@code cause}).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A {@code null} value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public HandlerCanNotBeCreatedException(Throwable cause) {
        super(cause);
    }

    protected HandlerCanNotBeCreatedException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
