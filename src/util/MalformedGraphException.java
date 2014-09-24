package util;

/**
 * Represents an error encountered when reading in a saved graph.
 */
public class MalformedGraphException extends Exception {

    /**
     * Constructs a new exception with the given error message.
     *
     * @param message the error message
     */
    public MalformedGraphException(String message) {
        super(message);
    }
}
