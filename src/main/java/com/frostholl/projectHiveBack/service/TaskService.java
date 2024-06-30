package com.frostholl.projectHiveBack.service;

import com.frostholl.projectHiveBack.exception.task.IncorrectDeadlineException;
import com.frostholl.projectHiveBack.exception.task.TaskNotFoundException;
import com.frostholl.projectHiveBack.model.Task;
import com.frostholl.projectHiveBack.model.Team;
import com.frostholl.projectHiveBack.model.TeamMember;
import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;

    public Task getTaskById(Integer id) {
        return repository.findTaskById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task was not found."));
    }

    public void addNewTask(TeamMember publisher,
                           String taskName,
                           String desc,
                           Integer diff,
                           ZonedDateTime deadline) {
        if (deadline.isBefore(ZonedDateTime.now())) {
            throw new IncorrectDeadlineException("Specified deadline is already passed.");
        }
        var task = Task.builder()
                .name(taskName)
                .description(desc)
                .difficulty(diff)
                .postTime(ZonedDateTime.now())
                .deadline(deadline)
                .publisher(publisher)
                .team(publisher.getTeam())
                .build();
        repository.save(task);
    }

    public List<Task> getAllTeamTasks(Team team) {
        return repository
                .findAll()
                .stream()
                .filter(task -> Objects.equals(task.getTeam().getId(), team.getId()))
                .toList();
    }

    public List<Task> getActiveTeamTasks(Team team) {
        return repository
                .findAll()
                .stream()
                .filter(task -> Objects.equals(task.getTeam().getId(), team.getId()) && !task.isFinished())
                .toList();
    }

    public List<Task> getPickedTeamTasks(Team team) {
        return repository
                .findAll()
                .stream()
                .filter(task -> Objects.equals(task.getTeam().getId(), team.getId()) && task.isPicked() && !task.isFinished())
                .toList();
    }

    public List<Task> getFinishedTeamTasks(Team team) {
        return repository
                .findAll()
                .stream()
                .filter(task -> Objects.equals(task.getTeam().getId(), team.getId()) && task.isPicked() && task.isFinished())
                .toList();
    }

    public void pickTask(TeamMember teamMember, Task task) {
        task.setExecutor(teamMember);
        repository.save(task);
    }

    public void refuseTask(Task task) {
        task.setExecutor(null);
        repository.save(task);
    }

    public void finishTask(Task task) {
        task.setFinishedTime(ZonedDateTime.now());
        repository.save(task);
    }

    public List<Task> getUserPickedTasks(User user) {
        return repository
                .findAll()
                .stream()
                .filter(task -> task.getExecutor() != null)
                .filter(task -> Objects.equals(task.getExecutor().getUser().getId(), user.getId()) && !task.isFinished())
                .toList();
    }
}
