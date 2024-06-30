package com.frostholl.projectHiveBack.controller;

import com.frostholl.projectHiveBack.exception.team.InviteCodeNotFoundOrExpiredException;
import com.frostholl.projectHiveBack.exception.team.NonTeamMemberAccessException;
import com.frostholl.projectHiveBack.model.Team;
import com.frostholl.projectHiveBack.model.TeamRole;
import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.request.AddNewTeamRequest;
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
                .admin(team.getAdmin())
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
            throw new InviteCodeNotFoundOrExpiredException("Invite code was not found or expired.");
        }
        service.userJoinTeam(user, code.getTeam());
        return ResponseEntity.ok("Joined to the team.");
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
                            .admin(team.getAdmin())
                            .memberList(service.getTeamMembers(team))
                            .inviteCode(isModOrAdmin ? team.getInviteCode() : null)
                            .activeTasks(taskService.getActiveTeamTasks(team))
                            .build();
                }
        ).toList();
        return ResponseEntity.ok(response);
    }
}
