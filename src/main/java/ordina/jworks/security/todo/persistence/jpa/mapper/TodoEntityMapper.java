package ordina.jworks.security.todo.persistence.jpa.mapper;

import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.persistence.jpa.entity.TodoEntity;
import org.mapstruct.Mapper;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@Mapper(componentModel = "spring")
public interface TodoEntityMapper {

    Todo mapEntityToModel(TodoEntity entity);

    TodoEntity mapModelToEntity(Todo todo);
}
