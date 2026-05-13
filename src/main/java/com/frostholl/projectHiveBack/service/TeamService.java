package com.frostholl.projectHiveBack.service;

import com.frostholl.projectHiveBack.exception.auth.UserNotFoundException;
import com.frostholl.projectHiveBack.exception.task.InsufficientRightsException;
import com.frostholl.projectHiveBack.exception.team.NonTeamMemberAccessException;
import com.frostholl.projectHiveBack.exception.team.TeamAdminNotFoundException;
import com.frostholl.projectHiveBack.exception.team.TeamNotFoundException;
import com.frostholl.projectHiveBack.exception.team.UserIsAlreadyMemberException;
import com.frostholl.projectHiveBack.model.Team;
import com.frostholl.projectHiveBack.model.TeamMember;
import com.frostholl.projectHiveBack.model.TeamRole;
import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository repository;

    private final TeamMemberService teamMemberService;

    private final InviteCodeService inviteCodeService;

    private final TaskService taskService;

    public Team getTeamById(Integer id) {
        var team = repository.findTeamById(id);
        return team.orElseThrow(() -> new TeamNotFoundException("Team was not found."));
    }

    public boolean isUserATeamMember(User user, Team team) {
        var members = getTeamMembers(team);
        return members
                .stream()
                .anyMatch(member -> Objects.equals(member.getUser().getId(), user.getId()));
    }

    public TeamRole getUsersTeamRole(User user, Team team) {
        var member = getTeamMembers(team)
                .stream()
                .filter(mem -> Objects.equals(mem.getUser().getId(), user.getId()))
                .findFirst()
                .orElseThrow(() -> new NonTeamMemberAccessException("Attempting to access a team without being a member of it."));
        return member.getRole();
    }

    public void updateTeam(Team team) {
        repository.save(team);
    }

    public void addNewTeam(User creator, String teamName) {
        var team = Team.builder()
                .name(teamName)
                .build();
        var admin = TeamMember.builder()
                .user(creator)
                .team(team)
                .role(TeamRole.ADMINISTRATOR)
                .build();
        var addedTeam = repository.save(team);
        teamMemberService.addNewTeamMember(admin);
        addedTeam.setInviteCode(inviteCodeService.addNewInviteCode(addedTeam));
        repository.save(addedTeam);
    }

    public List<TeamMember> getTeamMembers(Team team) {
        return teamMemberService.getAllMembersOfTeam(team);
    }

    public TeamMember getTeamAdmin(Team team) {
        var members = teamMemberService.getAllMembersOfTeam(team);
        return members
                .stream()
                .filter(member -> member.getRole() == TeamRole.ADMINISTRATOR)
                .findFirst()
                .orElseThrow(() -> new TeamAdminNotFoundException("Admin was not found"));
    }

    public TeamMember getUserAsTeamMember(User user, Team team) {
        var members = getTeamMembers(team);
        var userAsMember = members
                .stream()
                .filter(member -> Objects.equals(member.getUser().getId(), user.getId())).findFirst();
        return userAsMember
                .orElseThrow(() -> new NonTeamMemberAccessException("Attempting to access a team without being a member of it."));
    }

    public void userJoinTeam(User user, Team team) {
        if (isUserATeamMember(user, team)) {
            throw new UserIsAlreadyMemberException("User is already a team member.");
        }
        teamMemberService.addNewTeamMember(user, team, TeamRole.MEMBER);
    }

    public List<Team> getUsersTeams(User user) {
        var members = teamMemberService.getTeamMembersByUser(user);
        return members.stream().map(TeamMember::getTeam).toList();
    }

    public void deleteTeam(Team team) {
        var tasks = taskService.getAllTeamTasks(team);
        for (var task : tasks) {
            taskService.deleteTask(task);
        }
        var members = teamMemberService.getAllMembersOfTeam(team);
        for (var member : members) {
            teamMemberService.deleteTeamMember(member);
        }
        if (team.getInviteCode() != null) {
            inviteCodeService.deleteInviteCode(team.getInviteCode());
        }
        repository.delete(team);
    }

    public boolean tryGrantAdminRoleOnLeave(User adminToLeave, Team team) {
        var otherMembers = getTeamMembers(team)
                .stream()
                .filter(member -> !Objects.equals(member.getUser().getId(), adminToLeave.getId()))
                .toList();
        if (otherMembers.isEmpty()) {
            return false;
        }
        var mods = otherMembers
                .stream()
                .filter(member -> member.getRole() == TeamRole.MODERATOR)
                .toList();
        TeamMember newAdmin;
        if (mods.isEmpty()) {
            newAdmin = otherMembers.stream().findFirst().get();
            teamMemberService.grantTeamMember(newAdmin, TeamRole.ADMINISTRATOR);
        } else {
            newAdmin = mods.stream().findFirst().get();
            teamMemberService.grantTeamMember(newAdmin, TeamRole.ADMINISTRATOR);
        }
        teamMemberService.deleteTeamMember(getUserAsTeamMember(adminToLeave, team));
        return true;
    }

    public void kickMemberFromTeam(TeamMember teamMember) {
        var memberTasks = taskService.getAllTeamTasks(teamMember.getTeam())
                .stream()
                .filter(task -> Objects.equals(task.getPublisher().getId(), teamMember.getId())
                        || Objects.equals(task.getExecutor().getId(), teamMember.getId()))
                .toList();
        for (var task: memberTasks)
            taskService.deleteTask(task);
        teamMemberService.deleteTeamMember(teamMember);
    }

    public void kickMemberFromTeam(User user, Team team) {
        var teamMember = getUserAsTeamMember(user, team);
        kickMemberFromTeam(teamMember);
    }

    public void kickUserFromTeam(String userToKickLogin, Team team) {
        var members = getTeamMembers(team);
        var memberToKick = members
                .stream()
                .filter(member -> Objects.equals(member.getUser().getLogin(), userToKickLogin))
                .findFirst().orElseThrow(() -> new UserNotFoundException("User not found."));

        if (memberToKick.getRole() == TeamRole.ADMINISTRATOR) {
            throw new InsufficientRightsException("Insufficient rights.");
        }
        kickMemberFromTeam(memberToKick);
    }

    public void deleteInviteCode(Team team) {
        if (inviteCodeService.doesTeamHaveInviteCode(team)) {
            var inviteCode = inviteCodeService.getTeamInviteCode(team);
            team.setInviteCode(null);
            repository.save(team);
            inviteCodeService.deleteInviteCode(inviteCode);
        }
    }
}
