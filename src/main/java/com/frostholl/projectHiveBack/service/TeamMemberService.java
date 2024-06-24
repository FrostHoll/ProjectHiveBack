package com.frostholl.projectHiveBack.service;

import com.frostholl.projectHiveBack.model.Team;
import com.frostholl.projectHiveBack.model.TeamMember;
import com.frostholl.projectHiveBack.model.TeamRole;
import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TeamMemberService {
    private final TeamMemberRepository repository;

    public void addNewTeamMember(User user, Team team, TeamRole role) {
        var member = TeamMember.builder()
                .user(user)
                .team(team)
                .role(role)
                .build();
        repository.save(member);
    }

    public void addNewTeamMember(TeamMember teamMember) {
        repository.save(teamMember);
    }

    public List<TeamMember> getAllMembersOfTeam(Team team) {
        var members = repository.findAll()
                .stream()
                .filter((member) -> Objects.equals(member.getTeam().getId(), team.getId()));
        return members.toList();
    }
}
