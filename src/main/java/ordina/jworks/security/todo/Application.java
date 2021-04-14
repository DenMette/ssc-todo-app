package ordina.jworks.security.todo;

import ordina.jworks.security.todo.persistence.jpa.TodoRepository;
import ordina.jworks.security.todo.persistence.jpa.entity.TodoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    TodoRepository repository;

    @Override
    public void run(String... args) throws Exception {
        // Adding tasks into our database.
        this.repository.save(create("Task 1"));
        this.repository.save(create("Task 2"));
        TodoEntity entity = create("Task 3");
        entity.setCompleted(true);
        this.repository.save(entity);
        this.repository.save(create("Task 4"));
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
