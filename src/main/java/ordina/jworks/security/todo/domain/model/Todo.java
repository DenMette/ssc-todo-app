package ordina.jworks.security.todo.domain.model;

import ordina.jworks.security.todo.Default;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Maarten Casteels
 * @since 2021
 */
public class Todo {

    private final UUID id;
    private final String description;
    private final boolean completed;

    @Default
    public Todo(UUID id, String description, boolean completed) {
        this.id = id;
        this.description = description;
        this.completed = completed;
    }

    public Todo(String description) {
        this.id = null;
        this.description = description;
        this.completed = false;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(getDescription(), todo.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription());
    }

    @Override
    public String toString() {
        return "Todo{" +
                "description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }

    public Todo complete() {
        return new Todo(this.getId(), this.getDescription(), true);
    }
}
