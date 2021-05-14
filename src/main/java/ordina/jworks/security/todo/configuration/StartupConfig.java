package ordina.jworks.security.todo.configuration;

import ordina.jworks.security.todo.persistence.jpa.TodoRepository;
import ordina.jworks.security.todo.persistence.jpa.entity.TodoEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class StartupConfig {

    private final TodoRepository repository;

    public StartupConfig(TodoRepository repository) {
        this.repository = repository;
    }

    @Bean
    public CommandLineRunner run() throws Exception {
        return (args) -> {
            // Adding tasks into our database.
            this.repository.save(create("Task 1"));
            this.repository.save(create("Task 2"));
            TodoEntity entity = create("Task 3");
            entity.setCompleted(true);
            this.repository.save(entity);
            this.repository.save(create("Task 4"));
        };
    }

    private static TodoEntity create(final String description) {
        TodoEntity entity = new TodoEntity();
        entity.setDescription(description);
        entity.setCreatedAt(Instant.now());
        entity.setModifiedAt(entity.getCreatedAt());
        entity.setCompleted(false);
        return entity;
    }
}
