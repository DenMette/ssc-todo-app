package ordina.jworks.security.todo.web.in.resource;

import java.util.Objects;

/**
 * @author Maarten Casteels
 * @since 2021
 */
public class TodoResource {

    private final Long id;
    private final String description;
    private final boolean completed;

    public TodoResource(Long id, String description, boolean completed) {
        this.id = id;
        this.description = description;
        this.completed = completed;
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
        TodoResource todo = (TodoResource) o;
        return Objects.equals(getDescription(), todo.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription());
    }

    @Override
    public String toString() {
        return "TodoResource{" +
                "description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }
}
