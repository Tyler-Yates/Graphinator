package util;

/**
 * Represents an error encountered when reading in a saved graph.
 */
public class MalformedGraphException extends Exception {

    private static final long serialVersionUID = 543155159926511493L;

    /**
     * Constructs a new exception with the given error message.
     *
     * @param message the error message
     */
    public MalformedGraphException(String message) {
        super(message);
    }
}
