package de.neuefische.backend.service;

import de.neuefische.backend.dto.TodoDto;
import de.neuefische.backend.model.Todo;
import de.neuefische.backend.repo.TodoRepo;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TodoServiceTest {

    TodoRepo todoRepo = mock(TodoRepo.class);
    IdService idService = mock(IdService.class);
    TodoService todoService = new TodoService(todoRepo, idService);

    @Test
    void testUpdateTodo() {
        // GIVEN
        TodoDto todoToUpdate = new TodoDto();
        todoToUpdate.setId("123ABC");
        todoToUpdate.setDescription("Aufräumen");
        todoToUpdate.setStatus("OPEN");

        Todo todoToSave = new Todo();
        todoToSave.setId("123ABC");
        todoToSave.setDescription("Aufräumen");
        todoToSave.setStatus("OPEN");

        Todo updatedTodoItem = new Todo();
        updatedTodoItem.setId("123ABC");
        updatedTodoItem.setDescription("Aufräumen");
        updatedTodoItem.setStatus("OPEN");

        when(todoRepo.existsById("123ABC")).thenReturn(true);
        when(todoRepo.save(todoToSave)).thenReturn(updatedTodoItem);

        // WHEN
        Todo actual = todoService.updateTodo(todoToUpdate);

        // THEN
        assertThat(actual, sameInstance(updatedTodoItem));
    }

    @Test
    void testUpdateTodo_elementNotFound() {
        // GIVEN
        TodoDto todoToUpdate = new TodoDto();
        todoToUpdate.setId("123ABC");
        todoToUpdate.setDescription("Aufräumen");
        todoToUpdate.setStatus("OPEN");

        when(todoRepo.existsById("123ABC")).thenThrow(NoSuchElementException.class);

        // WHEN
        assertThrows(NoSuchElementException.class, () -> {
            todoService.updateTodo(todoToUpdate);
        });
    }
}
