package ordina.jworks.security.todo.web.in;

import ordina.jworks.security.todo.domain.TodoService;
import ordina.jworks.security.todo.web.in.mapper.TodoResourceMapper;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import ordina.jworks.security.todo.web.in.resource.TodoResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    @GetMapping
    public List<TodoResource> all() {
        return this.mapper.mapModelsToResources(service.allTasks());
    }

    @PostMapping({"", "/"})
    public ResponseEntity<TodoResource> saveTask(@Valid @RequestBody CreateTodoResource resource) {
        final TodoResource task = this.mapper.mapModelToResource(
                this.service.createTask(this.mapper.mapResourceToModel(resource)));
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(location).body(task);
    }

    @GetMapping("/{id}")
    public TodoResource findById(@PathVariable("id") UUID id) {
        return this.mapper.mapModelToResource(this.service.findTaskById(id));
    }

    @PutMapping("/{id}/complete")
    public TodoResource completeTaskById(@PathVariable("id") UUID id) {
        return this.mapper.mapModelToResource(
                        this.service.completeTask(
                                this.service.findTaskById(id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTaskById(@PathVariable("id") UUID id) {
        this.service.removeTaskById(id);
    }
}
