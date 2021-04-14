package ordina.jworks.security.todo.domain.model;

import java.util.Objects;

/**
 * @author Maarten Casteels
 * @since 2021
 */
public class Todo {

    private final Long id;
    private final String description;
    private final boolean completed;

    public Todo(Long id, String description, boolean completed) {
        this.id = id;
        this.description = description;
        this.completed = completed;
    }

    public Todo(String description) {
        this.id = null;
        this.description = description;
        this.completed = false;
    }

    public Long getId() {
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
