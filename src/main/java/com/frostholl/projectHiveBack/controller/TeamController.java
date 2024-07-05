package com.frostholl.projectHiveBack.controller;

import com.frostholl.projectHiveBack.exception.task.InsufficientRightsException;
import com.frostholl.projectHiveBack.exception.team.IncorrectDataException;
import com.frostholl.projectHiveBack.exception.team.InviteCodeNotFoundOrExpiredException;
import com.frostholl.projectHiveBack.exception.team.NonTeamMemberAccessException;
import com.frostholl.projectHiveBack.model.InviteCode;
import com.frostholl.projectHiveBack.model.Task;
import com.frostholl.projectHiveBack.model.TeamRole;
import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.request.AddNewTeamRequest;
import com.frostholl.projectHiveBack.request.KickUserFromTeamRequest;
import com.frostholl.projectHiveBack.response.TeamInfoResponse;
import com.frostholl.projectHiveBack.service.InviteCodeService;
import com.frostholl.projectHiveBack.service.TaskService;
import com.frostholl.projectHiveBack.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {
    private final TeamService service;

    private final InviteCodeService inviteCodeService;

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TeamInfoResponse> getTeamInfo(
            @AuthenticationPrincipal User user,
            @PathVariable Integer id
    ) {
        var team = service.getTeamById(id);
        if (!service.isUserATeamMember(user, team)) {
            throw new NonTeamMemberAccessException("Attempting to access a team without being a member of it.");
        }
        TeamRole userRole = service.getUsersTeamRole(user, team);
        boolean isModOrAdmin = userRole != TeamRole.MEMBER;
        var response = TeamInfoResponse.builder()
                .id(team.getId())
                .teamName(team.getName())
                .memberList(service.getTeamMembers(team))
                .inviteCode(isModOrAdmin ? team.getInviteCode() : null)
                .activeTasks(taskService.getActiveTeamTasks(team))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addNewTeam(
            @AuthenticationPrincipal User user,
            @RequestBody AddNewTeamRequest request
    ) {
        if (request.getTeamName().isEmpty()) {
            throw new IncorrectDataException("Incorrect team name.");
        }
        service.addNewTeam(user, request.getTeamName());
        return ResponseEntity.ok("Team created.");
    }

    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<String> joinTeam(
            @AuthenticationPrincipal User user,
            @PathVariable String inviteCode
    ) {
        var code = inviteCodeService.getInviteCodeById(inviteCode);
        if (code.isExpired()) {
            inviteCodeService.deleteInviteCode(code);
            throw new InviteCodeNotFoundOrExpiredException("Invite code was not found or is expired.");
        }
        service.userJoinTeam(user, code.getTeam());
        return ResponseEntity.ok("Joined to the team.");
    }

    @PostMapping("/refresh-invite-code/{teamId}")
    public ResponseEntity<InviteCode> refreshTeamInviteCode(@AuthenticationPrincipal User user,
                                                            @PathVariable Integer teamId
    ) {
        var team = service.getTeamById(teamId);
        if (!service.isUserATeamMember(user, team)) {
            throw new NonTeamMemberAccessException("Attempting to access a team without being a member of it.");
        }
        TeamRole userRole = service.getUsersTeamRole(user, team);
        boolean isAdminOrMod = userRole != TeamRole.MEMBER;
        if (!isAdminOrMod) {
            throw new InsufficientRightsException("Insufficient rights.");
        }
        service.deleteInviteCode(team);
        var inviteCode = inviteCodeService.addNewInviteCode(team);
        return ResponseEntity.ok(inviteCode);
    }

    @GetMapping("/user-teams")
    public ResponseEntity<List<TeamInfoResponse>> getUserTeams(@AuthenticationPrincipal User user) {
        var teams = service.getUsersTeams(user);
        var response = teams.stream().map(team -> {
                    TeamRole userRole = service.getUsersTeamRole(user, team);
                    boolean isModOrAdmin = userRole != TeamRole.MEMBER;
                    return TeamInfoResponse.builder()
                            .id(team.getId())
                            .teamName(team.getName())
                            .memberList(service.getTeamMembers(team))
                            .inviteCode(isModOrAdmin ? team.getInviteCode() : null)
                            .activeTasks(taskService.getActiveTeamTasks(team))
                            .build();
                }
        ).toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{teamId}")
    public ResponseEntity<String> deleteTeam(@AuthenticationPrincipal User user,
                                             @PathVariable Integer teamId
    ) {
        var team = service.getTeamById(teamId);
        if (!service.isUserATeamMember(user, team)) {
            throw new NonTeamMemberAccessException("Attempting to access a team without being a member of it.");
        }
        TeamRole userRole = service.getUsersTeamRole(user, team);
        boolean isAdmin = userRole == TeamRole.ADMINISTRATOR;
        if (!isAdmin) {
            throw new InsufficientRightsException("Insufficient rights.");
        }
        service.deleteTeam(team);
        return ResponseEntity.ok("Team deleted.");
    }

    @PostMapping("/leave/{teamId}")
    public ResponseEntity<String> leaveTeam(@AuthenticationPrincipal User user,
                                            @PathVariable Integer teamId
    ) {
        var team = service.getTeamById(teamId);
        if (!service.isUserATeamMember(user, team)) {
            throw new NonTeamMemberAccessException("Attempting to access a team without being a member of it.");
        }
        TeamRole userRole = service.getUsersTeamRole(user, team);
        boolean isAdmin = userRole == TeamRole.ADMINISTRATOR;
        if (isAdmin) {
            if (!service.tryGrantAdminRoleOnLeave(user, team)) {
                service.deleteTeam(team);
            }
            return ResponseEntity.ok("Successfully left the team.");
        }
        service.kickMemberFromTeam(user, team);
        return ResponseEntity.ok("Successfully left the team.");
    }

    @PostMapping("/kick")
    public ResponseEntity<String> kickUserFromTeam(@AuthenticationPrincipal User user,
                                                   @RequestBody KickUserFromTeamRequest request
    ) {
        var team = service.getTeamById(request.getTeamId());
        if (!service.isUserATeamMember(user, team)) {
            throw new NonTeamMemberAccessException("Attempting to access a team without being a member of it.");
        }
        TeamRole userRole = service.getUsersTeamRole(user, team);
        boolean isAdminOrMod = userRole != TeamRole.MEMBER;
        if (!isAdminOrMod) {
            throw new InsufficientRightsException("Insufficient rights.");
        }
        service.kickUserFromTeam(request.getUserLogin(), team);
        return ResponseEntity.ok("Successfully kicked user from the team.");
    }

    @GetMapping("/not-approved-tasks/{teamId}")
    public ResponseEntity<List<Task>> getNotApprovedTasks(@AuthenticationPrincipal User user,
                                                          @PathVariable Integer teamId
    ) {
        var team = service.getTeamById(teamId);
        if (!service.isUserATeamMember(user, team)) {
            throw new NonTeamMemberAccessException("Attempting to access a team without being a member of it.");
        }
        var tasks = taskService.getNotApprovedTeamTasks(team);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/finished-tasks/{teamId}")
    public ResponseEntity<List<Task>> getFinishedTasks(@AuthenticationPrincipal User user,
                                                       @PathVariable Integer teamId
    ) {
        var team = service.getTeamById(teamId);
        if (!service.isUserATeamMember(user, team)) {
            throw new NonTeamMemberAccessException("Attempting to access a team without being a member of it.");
        }
        var tasks = taskService.getFinishedTeamTasks(team);
        return ResponseEntity.ok(tasks);
    }
}
