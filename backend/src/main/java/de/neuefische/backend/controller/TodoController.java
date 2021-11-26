package de.neuefische.backend.controller;

import de.neuefische.backend.dto.TodoDto;
import de.neuefische.backend.model.Todo;
import de.neuefische.backend.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public List<Todo> getTodos(){
        return todoService.getTodos();
    }

    @GetMapping("{id}")
    public Todo getTodo(@PathVariable String id){
        return todoService.getTodo(id);
    }

    @PostMapping
    public Todo addTodo(@RequestBody TodoDto todoDto){
        return todoService.addTodo(todoDto);
    }

    @PutMapping("{id}")
    public Todo updateTodo(@PathVariable String id, @RequestBody TodoDto todoDto){

        if(!id.equals(todoDto.getId())){
            throw new IllegalArgumentException("Could not update element! Path id does not match with element id in request body!");
        }

        return todoService.updateTodo(todoDto);
    }

    @DeleteMapping("{id}")
    public void deleteTodo(@PathVariable String id){
        todoService.deleteTodo(id);
    }

}
