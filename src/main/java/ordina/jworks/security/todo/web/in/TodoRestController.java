package ordina.jworks.security.todo.web.in;

import ordina.jworks.security.todo.domain.TodoService;
import ordina.jworks.security.todo.web.in.mapper.TodoResourceMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@RestController
@RequestMapping("/api/todo")
public class TodoRestController {

    private final TodoService service;
    private final TodoResourceMapper mapper;

    public TodoRestController(TodoService service, TodoResourceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }
}
