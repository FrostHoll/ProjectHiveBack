package com.frostholl.projectHiveBack.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddNewTeamRequest {
    private String teamName;
}
