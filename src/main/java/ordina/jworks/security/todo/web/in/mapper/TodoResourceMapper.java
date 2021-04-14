package ordina.jworks.security.todo.web.in.mapper;

import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import ordina.jworks.security.todo.web.in.resource.TodoResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@Component
public class TodoResourceMapper {

    public List<TodoResource> mapModelsToResources(List<Todo> todos) {
        return todos.stream()
                .map(this::mapModelToResource)
                .collect(Collectors.toList());
    }

    public TodoResource mapModelToResource(Todo todo) {
        if (todo == null) {
            throw new IllegalArgumentException("Please provide a valid todo!");
        }

        return new TodoResource(todo.getId(), todo.getDescription(), todo.isCompleted());
    }

    public Todo mapResourceToModel(CreateTodoResource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Please provice a valid resource!");
        }

        return new Todo(resource.getDescription());
    }
}
