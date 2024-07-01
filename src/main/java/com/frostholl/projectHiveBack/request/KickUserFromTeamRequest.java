package com.frostholl.projectHiveBack.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KickUserFromTeamRequest {
    private String userLogin;

    private Integer teamId;
}
