package ordina.jworks.security.todo.web.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ordina.jworks.security.todo.domain.TodoService;
import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.persistence.jpa.TodoRepository;
import ordina.jworks.security.todo.web.in.mapper.TodoResourceMapperImpl;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Maarten Casteels
 */
@WebMvcTest
@Import(TodoResourceMapperImpl.class)
class TodoRestControllerTest {
    public static final String UUID_AS_STRING = "7483adfe-6e3d-4323-985d-9fcd9bc3fd52";
    public static final UUID ID = UUID.fromString(UUID_AS_STRING);

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TodoService todoService;

    @MockBean
    TodoRepository repository;

    @Nested
    @DisplayName("Get tasks")
    class GettingAllTodo {

        @Test
        void empty_list_when_no_tasks_were_found() throws Exception {
            mockMvc.perform(get("/api/todo").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"))
            ;
        }

        @Test
        void one_element_in_the_list() throws Exception {
            Mockito.when(todoService.allTasks()).thenReturn(List.of(new Todo(ID, "Test", false)));

            mockMvc.perform(get("/api/todo").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").value(UUID_AS_STRING))
                    .andExpect(jsonPath("$[0].description").value("Test"))
                    .andExpect(jsonPath("$[0].completed").value(false))
            ;
        }
    }

    @Nested
    @DisplayName("Get task")
    class GettingTodo {

        @Test
        void one_element() throws Exception {
            Mockito.when(todoService.findTaskById(ID)).thenReturn(new Todo(ID, "Test", false));

            mockMvc.perform(get("/api/todo/" + UUID_AS_STRING).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("id").value(UUID_AS_STRING))
                    .andExpect(jsonPath("description").value("Test"))
                    .andExpect(jsonPath("completed").value(false))
            ;
        }
    }

    @Nested
    @DisplayName("Create a task")
    class TodoCreation {

        @Test
        void task_created() throws Exception {
            Mockito.when(todoService.createTask(new Todo("Test"))).thenReturn(new Todo(ID, "Test", false));

            mockMvc.perform(
                    post("/api/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CreateTodoResource("Test"))))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/api/todo/" + UUID_AS_STRING))
                    .andExpect(redirectedUrl("http://localhost/api/todo/" + UUID_AS_STRING))
                    .andExpect(jsonPath("id").value(UUID_AS_STRING))
                    .andExpect(jsonPath("description").value("Test"))
                    .andExpect(jsonPath("completed").value(false))
            ;
        }

        @Test
        void task_description_is_null() throws Exception {
            mockMvc.perform(
                    post("/api/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CreateTodoResource())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].status").value(HttpStatus.BAD_REQUEST.name()))
                    .andExpect(jsonPath("$[0].code").value("description"))
                    .andExpect(jsonPath("$[0].message").value("must not be null"))
                    .andExpect(jsonPath("$[0].exceptionType").value("org.springframework.web.bind.MethodArgumentNotValidException"))
            ;
        }

        @Test
        void task_description_has_invalid_size_1() throws Exception {
            mockMvc.perform(
                    post("/api/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CreateTodoResource("A"))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].status").value(HttpStatus.BAD_REQUEST.name()))
                    .andExpect(jsonPath("$[0].code").value("description"))
                    .andExpect(jsonPath("$[0].message").value("The task should be at least 3 characters long."))
                    .andExpect(jsonPath("$[0].exceptionType").value("org.springframework.web.bind.MethodArgumentNotValidException"))
            ;
        }

        @Test
        void task_description_has_invalid_size_2() throws Exception {
            mockMvc.perform(
                    post("/api/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CreateTodoResource("AB"))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].status").value(HttpStatus.BAD_REQUEST.name()))
                    .andExpect(jsonPath("$[0].code").value("description"))
                    .andExpect(jsonPath("$[0].message").value("The task should be at least 3 characters long."))
                    .andExpect(jsonPath("$[0].exceptionType").value("org.springframework.web.bind.MethodArgumentNotValidException"))
            ;
        }

        @Test
        void task_description_has_valid_size_3() throws Exception {
            Mockito.when(todoService.createTask(new Todo("ABC"))).thenReturn(new Todo(ID, "ABC", false));

            mockMvc.perform(
                    post("/api/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CreateTodoResource("ABC"))))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/api/todo/" + UUID_AS_STRING))
                    .andExpect(redirectedUrl("http://localhost/api/todo/" + UUID_AS_STRING))
                    .andExpect(jsonPath("id").value(UUID_AS_STRING))
                    .andExpect(jsonPath("description").value("ABC"))
                    .andExpect(jsonPath("completed").value(false))
            ;
        }
    }

    @Nested
    @DisplayName("Complete a task")
    class TodoCompleted {

        @Test
        void complete_task_with_redirect() throws Exception {
            final Todo task = new Todo(ID, "ABC", false);
            Mockito.when(todoService.findTaskById(ID)).thenReturn(task);
            Mockito.when(todoService.completeTask(task)).thenReturn(new Todo(ID, "ABC", true));

            mockMvc.perform(
                    put("/api/todo/" + UUID_AS_STRING + "/complete")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("id").value(UUID_AS_STRING))
                    .andExpect(jsonPath("description").value("ABC"))
                    .andExpect(jsonPath("completed").value(true))
            ;
        }
    }

    @Nested
    @DisplayName("Remove a task")
    class TodoRemoved {

        @Test
        void remove_task_with_redirect() throws Exception {
            mockMvc.perform(
                    delete("/api/todo/" + UUID_AS_STRING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$").doesNotHaveJsonPath())
            ;
        }
    }
}