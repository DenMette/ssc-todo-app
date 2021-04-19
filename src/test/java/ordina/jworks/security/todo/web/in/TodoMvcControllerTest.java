package ordina.jworks.security.todo.web.in;

import ordina.jworks.security.todo.domain.TodoService;
import ordina.jworks.security.todo.persistence.jpa.TodoRepository;
import ordina.jworks.security.todo.web.in.mapper.TodoResourceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("Todo Creation")
    class TodoCreation {

        @Test
        void something() throws Exception {

            mockMvc.perform(post("/todo"))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection());
        }
    }
}