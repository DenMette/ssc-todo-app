package ordina.jworks.security.todo.web.in;

import ordina.jworks.security.todo.domain.TodoService;
import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.web.in.mapper.TodoResourceMapper;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@Controller
@RequestMapping("/todo")
public class TodoMvcController {

    private final TodoService service;
    private final TodoResourceMapper mapper;

    public TodoMvcController(TodoService service, TodoResourceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping({"", "/", "/index"})
    public String allTodo(Model model) {
        model.addAttribute("tasks", this.mapper.mapModelsToResources(service.allTasks()));
        model.addAttribute("newTask", new CreateTodoResource());
        return "index";
    }

    @PostMapping({"", "/", "/index"})
    public String saveTask(@ModelAttribute CreateTodoResource resource) {
        this.service.createTask(this.mapper.mapResourceToModel(resource));
        return "redirect:/todo";
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable("id") Long id) {
        final Todo todo = this.service.findTaskById(id);
        this.service.completeTask(todo);

        return "redirect:/todo";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        this.service.findTaskById(id);
        this.service.removeTaskById(id);

        return "redirect:/todo";
    }
}
