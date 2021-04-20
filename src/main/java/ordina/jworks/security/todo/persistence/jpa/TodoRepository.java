package ordina.jworks.security.todo.persistence.jpa;

import ordina.jworks.security.todo.persistence.jpa.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Maarten Casteels
 * @since 2021
 */
public interface TodoRepository extends JpaRepository<TodoEntity, UUID> {
}
