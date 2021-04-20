package ordina.jworks.security.todo.exception;

/**
 * @author Maarten Casteels
 */
public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException() {
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}
