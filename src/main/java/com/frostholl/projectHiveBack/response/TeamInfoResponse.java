package com.frostholl.projectHiveBack.response;

import com.frostholl.projectHiveBack.model.InviteCode;
import com.frostholl.projectHiveBack.model.Task;
import com.frostholl.projectHiveBack.model.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamInfoResponse {
    public Integer id;

    public String teamName;

    public TeamMember admin;

    public List<TeamMember> memberList;

    public InviteCode inviteCode;

    public List<Task> activeTasks;
}
