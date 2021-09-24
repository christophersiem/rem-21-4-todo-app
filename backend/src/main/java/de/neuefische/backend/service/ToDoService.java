package de.neuefische.backend.service;

import de.neuefische.backend.model.ApiTask;
import de.neuefische.backend.model.Task;
import de.neuefische.backend.repo.ToDoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {
    private final ToDoRepo toDoRepo;

    @Autowired
    public ToDoService(ToDoRepo toDoRepo) {
        this.toDoRepo = toDoRepo;
    }

    public List<Task> showAllList() {
        return toDoRepo.showAllTasks();
    }

    public void add(ApiTask apiTask) {
        toDoRepo.add(apiTask);
    }

//    public void deleteTask(String id) {
//        toDoRepo.deleteTask(id);
//    }
}