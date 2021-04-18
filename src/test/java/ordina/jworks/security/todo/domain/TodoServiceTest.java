package ordina.jworks.security.todo.domain;

import ordina.jworks.security.todo.domain.model.Todo;
import ordina.jworks.security.todo.persistence.TodoPersistenceFacade;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author Maarten Casteels
 * @since 2021
 */
class TodoServiceTest {

    private TodoPersistenceFacade facade;

    private TodoService service;

    @BeforeEach
    void setUp() {
        this.facade = Mockito.mock(TodoPersistenceFacade.class);
        this.service = new TodoService(facade);
    }

    @AfterEach
    void tearDown() {
        Mockito.verifyNoMoreInteractions(facade);
    }

    @DisplayName("Create a task")
    @Nested
    class TodoCreation {

        @Test
        @DisplayName("It should be possible to create a task")
        void it_should_be_possible_to_create_a_task() {
            // Arrange
            final Todo task = new Todo("task 1");
            final Todo mockedTask = new Todo(1L, "task 1", false);
            Mockito.when(facade.create(task)).thenReturn(mockedTask);

            // Act
            Todo savedTask = service.createTask(task);

            // Assert
            assertThat(savedTask).isEqualTo(mockedTask);
            Mockito.verify(facade).create(task);
        }

        @Test
        @DisplayName("It isn't possible to create a task with an ID")
        void it_is_not_possible_to_create_a_task_with_an_id() {
            // Arrange
            final Todo task = new Todo(1L, "task 1", false);

            try {
                // Act
                service.createTask(task);
                fail("should fail");
            } catch (IllegalArgumentException e) {
                // Assert
                assertThat(e).hasMessage("You can't create a new task!");
            }
        }

        @Test
        @DisplayName("It isn't possible to create a task without a description")
        void it_is_not_possible_to_create_a_task_without_a_description() {
            // Arrange
            final Todo task = new Todo("");

            try {
                // Act
                service.createTask(task);
                fail("should fail");
            } catch (IllegalArgumentException e) {
                // Assert
                assertThat(e).hasMessage("You can't create a new task!");
            }
        }
    }
}