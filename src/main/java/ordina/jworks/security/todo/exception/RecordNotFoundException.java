package ordina.jworks.security.todo.exception;

import java.util.UUID;

/**
 * @author Maarten Casteels
 */
public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException() {
    }

    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(UUID id) {
        super(String. format("Record with id %s not found.", id));
    }
}
