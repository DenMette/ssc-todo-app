package ordina.jworks.security.todo.web.in.mvc;

import ordina.jworks.security.todo.exception.RecordNotFoundException;
import ordina.jworks.security.todo.persistence.jpa.TodoRepository;
import ordina.jworks.security.todo.persistence.jpa.entity.TodoEntity;
import ordina.jworks.security.todo.web.in.resource.CreateTodoResource;
import ordina.jworks.security.todo.web.in.resource.TodoResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoMvcControllerIntegrationTest {
    public static final String UUID_AS_STRING = "7483adfe-6e3d-4323-985d-9fcd9bc3fd52";

    @Autowired
    MockMvc mockMvc;

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

            assertThat(repository.count()).isEqualTo(1);
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

            assertThat(repository.count()).isEqualTo(0);
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

            assertThat(repository.count()).isEqualTo(0);
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

            assertThat(repository.count()).isEqualTo(0);
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

            assertThat(repository.count()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Complete a task")
    class TodoCompleted {

        @Test
        void complete_task_with_redirect() throws Exception {
            final TodoEntity task = insertTask();

            mockMvc.perform(
                    post("/todo/" + task.getId() + "/complete"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/todo"))
                    .andExpect(view().name("redirect:/todo"))
                    .andExpect(flash().attribute("task-completed", "The task has been marked completed."))
            ;
        }

        @Test
        void complete_task_that_does_not_exists() throws Exception {
            mockMvc.perform(
                    post("/todo/" + UUID_AS_STRING + "/complete"))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(result ->
                            assertThat(result.getResolvedException())
                                    .isInstanceOf(RecordNotFoundException.class)
                                    .hasMessage("No task found."))
                    .andExpect(view().name("error"))
                    .andExpect(model().attributeExists("exception", "url"))
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
                    post("/todo/" + task.getId() + "/remove"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/todo"))
                    .andExpect(view().name("redirect:/todo"))
                    .andExpect(flash().attribute("task-removed", "The task has been removed."))
            ;
        }

        @Test
        void remove_task_that_does_not_exists() throws Exception {
            mockMvc.perform(
                    post("/todo/" + UUID_AS_STRING + "/remove"))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(result ->
                            assertThat(result.getResolvedException())
                                    .isInstanceOf(RecordNotFoundException.class)
                                    .hasMessage("No task found."))
                    .andExpect(view().name("error"))
                    .andExpect(model().attributeExists("exception", "url"))
            ;
        }
    }
}