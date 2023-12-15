package com.github.the10xdevs.citadels.exceptions;

/**
 * Thrown to indicate that an action not permitted by the game rules has been performed
 */
public class IllegalActionException extends Exception {
    /**
     * Constructs an IllegalActionException with the specified detail message
     * @param message The detail message
     */
    public IllegalActionException(String message) {
        super(message);
    }

    /**
     * Constructs an IllegalActionException with the specified detail message and cause
     * @param message The detail message
     * @param cause The cause
     */
    public IllegalActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
