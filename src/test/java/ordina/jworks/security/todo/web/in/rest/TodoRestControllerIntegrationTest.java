package ordina.jworks.security.todo.web.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ordina.jworks.security.todo.persistence.jpa.TodoRepository;
import ordina.jworks.security.todo.persistence.jpa.entity.TodoEntity;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Maarten Casteels
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoRestControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TodoRepository repository;

    @BeforeEach
    void setUp() {
        this.repository.deleteAll();
    }

    TodoEntity insertTask() {
        final TodoEntity entity = new TodoEntity();
        entity.setDescription("Simple Task");
        entity.setCreatedAt(Instant.now());
        entity.setModifiedAt(entity.getCreatedAt());
        return this.repository.save(entity);
    }

    @Nested
    @DisplayName("Get tasks")
    class GettingAllTodo {

        @Test
        void no_content_when_no_task_were_found() throws Exception {
            mockMvc.perform(get("/api/todo").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$").doesNotHaveJsonPath())
            ;
        }

        @Test
        void one_element_in_the_list() throws Exception {
            insertTask();

            mockMvc.perform(get("/api/todo").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").isNumber())
                    .andExpect(jsonPath("$[0].description").value("Simple Task"))
                    .andExpect(jsonPath("$[0].completed").value(false))
            ;
        }
    }

    @Nested
    @DisplayName("Get task")
    class GettingTodo {

        @Test
        void one_element() throws Exception {
            final TodoEntity task = insertTask();

            mockMvc.perform(get("/api/todo/" + task.getId()).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.description").value("Simple Task"))
                    .andExpect(jsonPath("$.completed").value(false))
            ;
        }
    }

    @Nested
    @DisplayName("Create a task")
    class TodoCreation {

        @Test
        void task_created() throws Exception {
            mockMvc.perform(
                    post("/api/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CreateTodoResource("Test"))))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", startsWith("http://localhost/api/todo/")))
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.description").value("Test"))
                    .andExpect(jsonPath("$.completed").value(false))
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
            mockMvc.perform(
                    post("/api/todo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CreateTodoResource("ABC"))))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", startsWith("http://localhost/api/todo/")))
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.description").value("ABC"))
                    .andExpect(jsonPath("$.completed").value(false))
            ;
        }
    }

    @Nested
    @DisplayName("Complete a task")
    class TodoCompleted {

        @Test
        void complete_task_with_redirect() throws Exception {
            final TodoEntity task = insertTask();

            mockMvc.perform(
                    put("/api/todo/" + task.getId() + "/complete")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.description").value("Simple Task"))
                    .andExpect(jsonPath("$.completed").value(true))
            ;
        }
    }

    @Nested
    @DisplayName("Remove a task")
    class TodoRemoved {

        @Test
        void remove_task_with_redirect() throws Exception {
            final TodoEntity task = insertTask();

            mockMvc.perform(
                    delete("/api/todo/" + task.getId() + "/remove")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$").doesNotHaveJsonPath())
            ;
        }
    }
}