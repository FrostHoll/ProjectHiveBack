package com.frostholl.projectHiveBack.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Integer difficulty;

    private ZonedDateTime postTime;

    private ZonedDateTime deadline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id")
    private TeamMember publisher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "executor_id")
    private TeamMember executor = null;

    private ZonedDateTime finishedTime = null;

    public boolean isPicked() {
        return executor != null;
    }

    public boolean isFinished() {
        return finishedTime != null;
    }
}
