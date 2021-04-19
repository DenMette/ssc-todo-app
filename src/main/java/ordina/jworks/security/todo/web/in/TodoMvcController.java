package ordina.jworks.security.todo.web.in;

import ordina.jworks.security.todo.domain.TodoService;
import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.web.in.mapper.TodoResourceMapper;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import ordina.jworks.security.todo.web.in.resource.TodoResource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

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

    @ModelAttribute("newTask")
    CreateTodoResource newTask() {
        return new CreateTodoResource();
    }

    @ModelAttribute("tasks")
    List<TodoResource> tasks() {
        return this.mapper.mapModelsToResources(service.allTasks());
    }

    @GetMapping({"", "/"})
    public String allTodo() {
        return "index";
    }

    @PostMapping({"", "/"})
    public String saveTask(@Valid @ModelAttribute("newTask") CreateTodoResource resource, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        this.service.createTask(this.mapper.mapResourceToModel(resource));
        redirectAttributes.addFlashAttribute("flash", "Task has been added.");
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
