package com.frostholl.projectHiveBack.request;

import lombok.*;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddNewTaskRequest {
    private Integer teamId;

    private String taskName;

    private String description;

    private Integer difficulty;

    private ZonedDateTime deadline;
}
