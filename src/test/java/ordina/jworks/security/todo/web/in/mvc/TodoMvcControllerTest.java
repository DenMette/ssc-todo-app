package ordina.jworks.security.todo.web.in.mvc;

import ordina.jworks.security.todo.domain.TodoService;
import ordina.jworks.security.todo.persistence.jpa.TodoRepository;
import ordina.jworks.security.todo.web.in.mapper.TodoResourceMapperImpl;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import ordina.jworks.security.todo.web.in.resource.TodoResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@WebMvcTest
@Import(TodoResourceMapperImpl.class)
class TodoMvcControllerTest {
    public static final String UUID_AS_STRING = "7483adfe-6e3d-4323-985d-9fcd9bc3fd52";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WebClient webClient;

    @MockBean
    TodoService todoService;

    @MockBean
    TodoRepository repository;

    @Nested
    @DisplayName("Get tasks")
    class GettingTodo {

        @Test
        void root_redirects_to_todo_page() throws Exception {
            mockMvc.perform(get("/"))
                   .andExpect(status().is3xxRedirection())
                   .andExpect(redirectedUrl("/todo"));
        }

        @Test
        void get_empty_list() throws Exception {
            mockMvc.perform(get("/todo"))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name("index"))
                    .andExpect(model().attribute("tasks", Collections.<TodoResource>emptyList()))
                    .andExpect(model().attribute("newTask", new CreateTodoResource()))
            ;
        }
    }

    @Nested
    @DisplayName("Create a task")
    class TodoCreation {

        @Test
        void task_created() throws Exception {
            mockMvc.perform(
                    post("/todo")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("description", "A small test"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/todo"))
                    .andExpect(view().name("redirect:/todo"))
                    .andExpect(flash().attribute("taskCreated", "Task has been created."))
            ;
        }

        @Test
        void task_description_is_null() throws Exception {
            mockMvc.perform(
                    post("/todo")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name("index"))
                    .andExpect(model().errorCount(1))
                    .andExpect(model().attributeHasFieldErrorCode("newTask", "description", "NotNull"))
            ;
        }

        @Test
        void task_description_has_invalid_size_1() throws Exception {
            mockMvc.perform(
                    post("/todo")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("description", "A"))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name("index"))
                    .andExpect(model().errorCount(1))
                    .andExpect(model().attributeHasFieldErrorCode("newTask", "description", "Size"))
            ;
        }

        @Test
        void task_description_has_invalid_size_2() throws Exception {
            mockMvc.perform(
                    post("/todo")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("description", "AB"))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(view().name("index"))
                    .andExpect(model().errorCount(1))
                    .andExpect(model().attributeHasFieldErrorCode("newTask", "description", "Size"))
            ;
        }

        @Test
        void task_description_has_valid_size_3() throws Exception {
            mockMvc.perform(
                    post("/todo")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("description", "ABC"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/todo"))
                    .andExpect(view().name("redirect:/todo"))
                    .andExpect(flash().attribute("taskCreated", "Task has been created."))
            ;
        }
    }

    @Nested
    @DisplayName("Complete a task")
    class TodoCompleted {

        @Test
        void complete_task_with_redirect() throws Exception {
            mockMvc.perform(
                    post("/todo/" + UUID_AS_STRING + "/complete"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/todo"))
                    .andExpect(view().name("redirect:/todo"))
                    .andExpect(flash().attribute("taskCompleted", "The task has been marked completed."))
            ;
        }
    }

    @Nested
    @DisplayName("Remove a task")
    class TodoRemoved {

        @Test
        void remove_task_with_redirect() throws Exception {
            mockMvc.perform(
                    post("/todo/" + UUID_AS_STRING + "/remove"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/todo"))
                    .andExpect(view().name("redirect:/todo"))
                    .andExpect(flash().attribute("taskRemoved", "The task has been removed."))
            ;
        }
    }
}