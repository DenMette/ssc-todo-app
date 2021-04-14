package ordina.jworks.security.todo.persistence.jpa.mapper;

import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.persistence.jpa.entity.TodoEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@Component
public class TodoEntityMapper {

    public Todo mapEntityToModel(TodoEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Please provide a valid entity!");
        }

        return new Todo(entity.getId(), entity.getDescription(), entity.isCompleted());
    }

    public TodoEntity mapModelToEntity(Todo todo) {
        if (todo == null) {
            throw new IllegalArgumentException("Please provide a valid model!");
        }

        final TodoEntity entity = new TodoEntity();
        entity.setDescription(todo.getDescription());
        entity.setCompleted(false);
        entity.setCreatedAt(Instant.now());
        entity.setModifiedAt(entity.getCreatedAt());
        return entity;
    }
}
