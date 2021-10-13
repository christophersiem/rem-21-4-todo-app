package de.neuefische.backend.service;

import de.neuefische.backend.model.Todo;
import de.neuefische.backend.repo.TodoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepo todoRepo;
    private final IdService idService;

    @Autowired
    public TodoService(TodoRepo todoRepo, IdService idService) {
        this.todoRepo = todoRepo;
        this.idService = idService;
    }

    public List<Todo> getTodos() {
        return todoRepo.findAll();
    }

    public Todo addTodo(Todo todo) {
        todo.setId(idService.generateId());
        return todoRepo.save(todo);
    }

    public Todo updateTodo(Todo todo) {
        if(todoRepo.existsById(todo.getId())){
            return todoRepo.save(todo);
        } else{
            throw new NoSuchElementException("Could not update Todo element! Element with id does not exist: " + todo.getId());
        }
    }

    public void deleteTodo(String id) {
        todoRepo.deleteById(id);
    }

    public Todo getTodo(String id) {
        Optional<Todo> optionalTodo = todoRepo.findById(id);
        if (optionalTodo.isPresent()) {
            return optionalTodo.get();
        } else {
            throw new NoSuchElementException("Todo not found with id: " + id);
        }
    }
}
