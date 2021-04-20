package ordina.jworks.security.todo.domain;

import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.exception.RecordNotFoundException;
import ordina.jworks.security.todo.exception.ValidationException;
import ordina.jworks.security.todo.persistence.TodoPersistenceFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@Service
public class TodoService {

    private final TodoPersistenceFacade facade;

    public TodoService(TodoPersistenceFacade facade) {
        this.facade = facade;
    }

    public Todo createTask(final Todo task) {
        if (task.getId() != null) {
            throw new ValidationException("You can't create a new task!");
        }
        if (task.getDescription() == null || task.getDescription().isBlank()) {
            throw new ValidationException("You can't create a new task!");
        }

        return this.facade.create(task);
    }

    public Todo completeTask(final Todo task) {
        if (task.getId() == null) {
            throw new ValidationException("You can't complete the task!");
        }

        return this.facade.complete(task);
    }

    public List<Todo> allTasks() {
        return this.facade.findAll();
    }

    public Todo findTaskById(UUID id) {
        return this.facade.findById(id).orElseThrow(() -> new RecordNotFoundException("No task found."));
    }

    public void removeTaskById(UUID id) {
        this.facade.removeById(id);
    }
}
