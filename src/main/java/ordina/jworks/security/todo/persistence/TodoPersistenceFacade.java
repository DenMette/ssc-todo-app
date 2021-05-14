package ordina.jworks.security.todo.persistence;

import ordina.jworks.security.todo.domain.model.Todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Maarten Casteels
 * @since 2021
 */
public interface TodoPersistenceFacade {
    Todo create(Todo task);

    Todo complete(UUID id);

    List<Todo> findAll();

    Optional<Todo> findById(UUID id);

    void removeById(UUID id);
}
