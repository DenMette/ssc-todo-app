package ordina.jworks.security.todo.persistence.jpa;

import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.persistence.TodoPersistenceFacade;
import ordina.jworks.security.todo.persistence.jpa.entity.TodoEntity;
import ordina.jworks.security.todo.persistence.jpa.mapper.TodoEntityMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@Service
public class TodoPersistenceJpaService implements TodoPersistenceFacade {

    private final TodoRepository repository;
    private final TodoEntityMapper mapper;

    public TodoPersistenceJpaService(TodoRepository repository, TodoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Todo create(Todo task) {
        return this.mapper.mapEntityToModel(this.repository.save(this.mapper.mapModelToEntity(task)));
    }

    @Override
    public Todo complete(Todo task) {
        TodoEntity byId = this.repository.findById(task.getId()).get();

        byId.setModifiedAt(Instant.now());
        byId.setCompleted(true);

        return this.mapper.mapEntityToModel(this.repository.save(byId));
    }

    @Override
    public List<Todo> findAll() {
        return this.repository.findAll().stream()
                .map(mapper::mapEntityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Todo> findById(Long id) {
        return this.repository.findById(id).map(mapper::mapEntityToModel);
    }

    @Override
    public void removeById(Long id) {
        this.repository.deleteById(id);
    }
}
