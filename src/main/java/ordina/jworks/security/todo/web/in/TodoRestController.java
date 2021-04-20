package ordina.jworks.security.todo.web.in;

import ordina.jworks.security.todo.domain.TodoService;
import ordina.jworks.security.todo.web.in.mapper.TodoResourceMapper;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import ordina.jworks.security.todo.web.in.resource.TodoResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@RestController
@RequestMapping(value = "/api/todo", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class TodoRestController {

    private final TodoService service;
    private final TodoResourceMapper mapper;

    public TodoRestController(TodoService service, TodoResourceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<TodoResource>> all() {
        final List<TodoResource> resources = this.mapper.mapModelsToResources(service.allTasks());

        if (resources.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(resources);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<TodoResource> saveTask(@Valid @RequestBody CreateTodoResource resource) {
        final TodoResource task = this.mapper.mapModelToResource(
                this.service.createTask(this.mapper.mapResourceToModel(resource)));
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(location).body(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResource> findById(@Min(1) @PathVariable("id") Long id) {
        return ResponseEntity.ok(this.mapper.mapModelToResource(this.service.findTaskById(id)));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TodoResource> completeTaskById(@Min(1) @PathVariable("id") Long id) {
        return ResponseEntity.ok(
                this.mapper.mapModelToResource(
                        this.service.completeTask(
                                this.service.findTaskById(id))));
    }

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<TodoResource> removeTaskById(@Min(1) @PathVariable("id") Long id) {
        this.service.findTaskById(id);
        this.service.removeTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
