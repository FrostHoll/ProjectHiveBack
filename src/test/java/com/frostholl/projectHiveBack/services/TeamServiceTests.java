package com.frostholl.projectHiveBack.services;

import com.frostholl.projectHiveBack.model.*;
import com.frostholl.projectHiveBack.repository.TeamRepository;
import com.frostholl.projectHiveBack.service.InviteCodeService;
import com.frostholl.projectHiveBack.service.TaskService;
import com.frostholl.projectHiveBack.service.TeamMemberService;
import com.frostholl.projectHiveBack.service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTests {

    @Mock
    private TeamRepository repository;

    @Mock
    private TeamMemberService teamMemberService;

    @Mock
    private InviteCodeService inviteCodeService;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TeamService teamService;

    @Test
    public void whenGetTeamById_thenTeamReturned() {
        Integer id = 1;
        Team team = new Team(id, "Test team", null);

        Mockito.when(repository.findTeamById(id)).thenReturn(Optional.of(team));

        Team result = teamService.getTeamById(id);
        assertEquals(team, result);
    }

    @Test
    public void whenTeamAdded_thenCreatorIsAdmin() {
        User user = new User(1, "login", "pass", "name", true);
        Team team = new Team(1, "Team", null);
        TeamMember teamMember = new TeamMember(1, user, team, TeamRole.ADMINISTRATOR);

        Mockito.when(inviteCodeService.addNewInviteCode(Mockito.any())).thenReturn(null);
        Mockito.when(repository.save(Mockito.any())).thenReturn(team);
        Mockito.when(teamService.getTeamMembers(team)).thenReturn(List.of(teamMember));

        teamService.addNewTeam(user, "Team");
        Mockito.verify(teamMemberService).addNewTeamMember(Mockito.any());

        TeamRole role = teamService.getUsersTeamRole(user, team);
        assertEquals(TeamRole.ADMINISTRATOR, role);
    }

    @Test
    public void whenTryGranAdminOnLeave_thenGrantsRole() {
        User admin = new User(1, "login", "pass", "name", true);
        User user = new User(2, "login1", "pass", "name", true);
        Team team = new Team(1, "Team", null);
        TeamMember adminMember = new TeamMember(1, admin, team, TeamRole.ADMINISTRATOR);
        TeamMember userMember = new TeamMember(2, user, team, TeamRole.MEMBER);

        Mockito.when(teamService.getTeamMembers(team)).thenReturn(List.of(adminMember, userMember));

        boolean result = teamService.tryGrantAdminRoleOnLeave(admin, team);

        assertTrue(result);
        Mockito.verify(teamMemberService).grantTeamMember(userMember, TeamRole.ADMINISTRATOR);
        Mockito.verify(teamMemberService).deleteTeamMember(adminMember);
    }
}
