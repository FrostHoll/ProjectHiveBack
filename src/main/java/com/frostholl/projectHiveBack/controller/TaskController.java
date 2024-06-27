package com.frostholl.projectHiveBack.controller;

import com.frostholl.projectHiveBack.exception.task.InsufficientRightsException;
import com.frostholl.projectHiveBack.exception.task.TaskIsAlreadyFinishedException;
import com.frostholl.projectHiveBack.exception.task.TaskIsAlreadyTakenException;
import com.frostholl.projectHiveBack.exception.task.UnauthorizedTaskAccessException;
import com.frostholl.projectHiveBack.model.Task;
import com.frostholl.projectHiveBack.model.TeamRole;
import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.request.AddNewTaskRequest;
import com.frostholl.projectHiveBack.service.TaskService;
import com.frostholl.projectHiveBack.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService service;

    private final TeamService teamService;

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@AuthenticationPrincipal User user,
                                        @PathVariable Integer taskId) {
        var task = service.getTaskById(taskId);
        if (!teamService.isUserATeamMember(user, task.getTeam())) {
            throw new UnauthorizedTaskAccessException("Access to an unauthorized team's task.");
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addNewTask(@AuthenticationPrincipal User user,
                                             @RequestBody AddNewTaskRequest request) {
        var team = teamService.getTeamById(request.getTeamId());
        if (!teamService.isUserATeamMember(user, team)) {
            throw new UnauthorizedTaskAccessException("Access to an unauthorized team's task.");
        }
        var member = teamService.getUserAsTeamMember(user, team);
        if (member.getRole() == TeamRole.MEMBER) {
            throw new InsufficientRightsException("Insufficient rights.");
        }
        service.addNewTask(member,
                request.getTaskName(),
                request.getDescription(),
                request.getDifficulty(),
                request.getDeadline()
        );
        return ResponseEntity.ok("Task added.");
    }

    @PostMapping("/pick/{taskId}")
    public ResponseEntity<String> pickTask(@AuthenticationPrincipal User user,
                                           @PathVariable Integer taskId) {
        var task = service.getTaskById(taskId);
        var team = task.getTeam();
        if (!teamService.isUserATeamMember(user, team)) {
            throw new UnauthorizedTaskAccessException("Access to an unauthorized team's task.");
        }
        if (task.isPicked()) {
            throw new TaskIsAlreadyTakenException("Task is already taken.");
        }
        if (task.isFinished()) {
            throw new TaskIsAlreadyFinishedException("Task is already finished.");
        }
        var member = teamService.getUserAsTeamMember(user, team);
        service.pickTask(member, task);
        return ResponseEntity.ok("Task has been picked.");
    }

    @PostMapping("/refuse/{taskId}")
    public ResponseEntity<String> refuseTask(@AuthenticationPrincipal User user,
                                             @PathVariable Integer taskId) {
        var task = service.getTaskById(taskId);
        var team = task.getTeam();
        if (!teamService.isUserATeamMember(user, team)) {
            throw new UnauthorizedTaskAccessException("Access to an unauthorized team's task.");
        }
        if (task.isFinished()) {
            throw new TaskIsAlreadyFinishedException("Task is already finished.");
        }
        var member = teamService.getUserAsTeamMember(user, team);
        if (!Objects.equals(task.getExecutor().getId(), member.getId())) {
            throw new InsufficientRightsException("Insufficient rights.");
        }
        service.refuseTask(task);
        return ResponseEntity.ok("Task has been refused.");
    }

    @PostMapping("finish/{taskId}")
    public ResponseEntity<String> finishTask(@AuthenticationPrincipal User user,
                                             @PathVariable Integer taskId) {
        var task = service.getTaskById(taskId);
        var team = task.getTeam();
        if (!teamService.isUserATeamMember(user, team)) {
            throw new UnauthorizedTaskAccessException("Access to an unauthorized team's task.");
        }
        if (task.isFinished()) {
            throw new TaskIsAlreadyFinishedException("Task is already finished.");
        }
        var member = teamService.getUserAsTeamMember(user, team);
        if (!Objects.equals(task.getExecutor().getId(), member.getId())) {
            throw new InsufficientRightsException("Insufficient rights.");
        }
        service.finishTask(task);
        return ResponseEntity.ok("Task has been finished.");
    }

    @GetMapping("/picked")
    public List<Task> getUsersPickedTasks(@AuthenticationPrincipal User user) {
        return service.getUserPickedTasks(user);
    }
}
