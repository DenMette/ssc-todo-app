package ordina.jworks.security.todo.persistence.jpa;

import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.exception.RecordNotFoundException;
import ordina.jworks.security.todo.persistence.TodoPersistenceFacade;
import ordina.jworks.security.todo.persistence.jpa.mapper.TodoEntityMapper;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public Todo complete(@NotNull UUID id) throws RecordNotFoundException {
        return this.repository.findById(id).map(byId -> {
            byId.setModifiedAt(Instant.now());
            byId.setCompleted(true);
            return this.mapper.mapEntityToModel(this.repository.save(byId));
        }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    @Override
    public List<Todo> findAll() {
        return this.repository.findAll().stream()
                .map(mapper::mapEntityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Todo> findById(UUID id) {
        return this.repository.findById(id).map(mapper::mapEntityToModel);
    }

    @Override
    public void removeById(UUID id) {
        this.repository.deleteById(id);
    }
}
