package com.frostholl.projectHiveBack.repository;

import com.frostholl.projectHiveBack.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findTaskById(Integer id);
}
