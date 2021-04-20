package ordina.jworks.security.todo.web.in;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeMvcController {

    @GetMapping
    public String getHome() {
        return "redirect:/todo";
    }
}
