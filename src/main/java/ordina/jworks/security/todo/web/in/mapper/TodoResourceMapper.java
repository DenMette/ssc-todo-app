package ordina.jworks.security.todo.web.in.mapper;

import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import ordina.jworks.security.todo.web.in.resource.TodoResource;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@Mapper(componentModel = "spring")
public interface TodoResourceMapper {

    List<TodoResource> mapModelsToResources(List<Todo> todos);

    TodoResource mapModelToResource(Todo todo);

    Todo mapResourceToModel(CreateTodoResource resource);
}
