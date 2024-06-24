package com.frostholl.projectHiveBack.service;

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

    public List<Team> getAllTeams() {
        return repository.findAll();
    }

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
        addedTeam.setAdmin(admin);
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
}
