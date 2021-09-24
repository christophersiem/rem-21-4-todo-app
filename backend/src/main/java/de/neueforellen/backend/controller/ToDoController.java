package de.neueforellen.backend.controller;

import de.neueforellen.backend.model.ToDo;
import de.neueforellen.backend.service.ToDoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ToDoController {

    ToDoService service;

    public ToDoController(ToDoService service) {
        this.service = service;
    }

    @GetMapping("api/todo")
    public List<ToDo> getAllTodos(){
        //TODO get all ToDos
        // service.getAllToDos() -> return List<ToDos>
        return List.of(new ToDo());
    }

    @GetMapping("api/todo/{id}")
    public ToDo getToDoByID(@PathVariable String id){
        //TODO find by ID
        // service.getToDoByID(String id) -> return ToDo
        return new ToDo();
    }

    @DeleteMapping("api/todo/{id}")
    public void deleteToDoByID(@PathVariable String id){
        //TODO delete
        // service.deleteToDoByID(String id) -> void
    }

    @PostMapping("api/todo")
    public ToDo addTodo(@RequestParam ToDo toAdd){
        //TODO add new Todo
        // service.addToDo(ToDo) -> return ToDo
        return new ToDo();
    }

    @PutMapping("api/todo/{id}")
    public ToDo editToDoStatus(@PathVariable String id, String status){
        //TODO edit existing ToDO
        //service.editToDo(String id) -> return ToDo
        return new ToDo();
    }


}
