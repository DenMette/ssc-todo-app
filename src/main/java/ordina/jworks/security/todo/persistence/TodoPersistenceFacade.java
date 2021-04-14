package ordina.jworks.security.todo.persistence;

import ordina.jworks.security.todo.domain.model.Todo;

import java.util.List;
import java.util.Optional;

/**
 * @author Maarten Casteels
 * @since 2021
 */
public interface TodoPersistenceFacade {
    Todo create(Todo task);

    Todo complete(Todo task);

    List<Todo> findAll();

    Optional<Todo> findById(Long id);

    void removeById(Long id);
}
