package ordina.jworks.security.todo.web.in.mvc;

import ordina.jworks.security.todo.domain.TodoService;
import ordina.jworks.security.todo.persistence.jpa.TodoRepository;
import ordina.jworks.security.todo.web.in.mapper.TodoResourceMapper;
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
@Import(TodoResourceMapper.class)
class TodoMvcControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    @MockBean
    TodoRepository repository;

    @Nested
    @DisplayName("Get tasks")
    class GettingTodo {

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
                    .andExpect(flash().attribute("task-created", "Task has been created."))
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
                    .andExpect(flash().attribute("task-created", "Task has been created."))
            ;
        }
    }

    @Nested
    @DisplayName("Complete a task")
    class TodoCompleted {

        @Test
        void complete_task_with_redirect() throws Exception {
            mockMvc.perform(
                    post("/todo/1/complete"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/todo"))
                    .andExpect(view().name("redirect:/todo"))
                    .andExpect(flash().attribute("task-completed", "The task has been marked completed."))
            ;
        }
    }

    @Nested
    @DisplayName("Remove a task")
    class TodoRemoved {

        @Test
        void remove_task_with_redirect() throws Exception {
            mockMvc.perform(
                    post("/todo/1/remove"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/todo"))
                    .andExpect(view().name("redirect:/todo"))
                    .andExpect(flash().attribute("task-removed", "The task has been removed."))
            ;
        }
    }
}