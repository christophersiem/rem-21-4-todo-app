package de.neueforellen.backend.service;

import de.neueforellen.backend.model.ToDo;
import de.neueforellen.backend.repo.ToDoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {

    private final ToDoRepo toDoRepo;

    @Autowired
    public ToDoService(ToDoRepo toDoRepo) {
        this.toDoRepo = toDoRepo;
    }

    public List<ToDo> getAllTodos(){
        return toDoRepo.getAllTodos;
    }

    public ToDo getToDoById(String id) throws IllegalArgumentException{
        Optional<ToDo> optionalToDo = toDoRepo.findById(id);
        if(optionalToDo.isPresent()) {
            return optionalToDo.get();
        }
        throw new IllegalArgumentException("Todo not found by id: " + id);
    }

    public void deleteToDoByID(String id) throws IllegalArgumentException{
        Optional<ToDo> optionalToDo = toDoRepo.findById(id);
        if(optionalToDo.isPresent()){
            toDoRepo.delete(optionalToDo.get());
        }
        throw new IllegalArgumentException("ToDo not deleted by id: " + id);
    }

    public ToDo addTodo(ToDo toDo){
        return toDoRepo.add(toDo);
    }

    public ToDo editToDoStatus(String id, String status){
        return toDoRepo.edit(id, status);
    }

}
