package ordina.jworks.security.todo.web.in.resource;

import java.util.Objects;

/**
 * @author Maarten Casteels
 * @since 2021
 */
public class CreateTodoResource {

    private String description;

    public CreateTodoResource() {
    }

    public CreateTodoResource(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateTodoResource that = (CreateTodoResource) o;
        return Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription());
    }

    @Override
    public String toString() {
        return "CreateTodoResource{" +
                "description='" + description + '\'' +
                '}';
    }
}
