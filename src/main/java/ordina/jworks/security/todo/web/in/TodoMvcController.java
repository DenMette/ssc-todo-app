package ordina.jworks.security.todo.web.in;

import ordina.jworks.security.todo.domain.TodoService;
import ordina.jworks.security.todo.web.in.mapper.TodoResourceMapper;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import ordina.jworks.security.todo.web.in.resource.TodoResource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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

    @GetMapping
    public String allTodo() {
        return "index";
    }

    @PostMapping
    public String saveTask(@Valid @ModelAttribute("newTask") CreateTodoResource resource, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        this.service.createTask(this.mapper.mapResourceToModel(resource));
        redirectAttributes.addFlashAttribute("taskCreated", "Task has been created.");
        return "redirect:/todo";
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        this.service.completeTask(id);

        redirectAttributes.addFlashAttribute("taskCompleted", "The task has been marked completed.");
        return "redirect:/todo";
    }

    @PostMapping("/{id}/remove")
    public String remove(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        this.service.findTaskById(id);
        this.service.removeTaskById(id);

        redirectAttributes.addFlashAttribute("taskRemoved", "The task has been removed.");
        return "redirect:/todo";
    }
}
