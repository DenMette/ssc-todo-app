package ordina.jworks.security.todo.domain.model;


import java.util.Objects;
import java.util.UUID;

/**
 * @author Maarten Casteels
 * @since 2021
 */
public class Todo {

    private UUID id;
    private String description;
    private boolean completed;

    public Todo() {
    }

    public Todo(UUID id, String description, boolean completed) {
        this.id = id;
        this.description = description;
        this.completed = completed;
    }

    public Todo(String description) {
        this(null, description, false);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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
