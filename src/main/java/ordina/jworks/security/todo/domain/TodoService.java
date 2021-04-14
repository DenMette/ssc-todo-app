package ordina.jworks.security.todo.domain;

import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.persistence.TodoPersistenceFacade;
import org.springframework.stereotype.Service;

import java.util.List;

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
            throw new IllegalArgumentException("You can't create a new task!");
        }

        return this.facade.create(task);
    }

    public Todo completeTask(final Todo task) {
        if (task.getId() == null) {
            throw new IllegalArgumentException("You can't complete the task!");
        }

        return this.facade.complete(task);
    }

    public List<Todo> allTasks() {
        return this.facade.findAll();
    }

    public Todo findTaskById(Long id) {
        return this.facade.findById(id).orElseThrow(() -> new IllegalArgumentException("No task found."));
    }

    public void removeTaskById(Long id) {
        this.facade.removeById(id);
    }
}
