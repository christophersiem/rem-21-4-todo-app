package de.neuefische.backend.repo;

import de.neuefische.backend.model.Todo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepo extends PagingAndSortingRepository<Todo, String> {

    List<Todo> findAll();
}
