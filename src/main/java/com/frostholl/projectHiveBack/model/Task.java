package com.frostholl.projectHiveBack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private boolean approved = false;

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

    @Enumerated(EnumType.STRING)
    public TaskStatus getStatus() {
        if (approved) return TaskStatus.APPROVED;
        if (finishedTime != null) return TaskStatus.FINISHED;
        if (executor != null) return TaskStatus.PICKED;
        return TaskStatus.PUBLISHED;
    }

    @JsonIgnore
    @Transient
    public boolean isPicked() {
        return executor != null;
    }

    @JsonIgnore
    @Transient
    public boolean isFinished() {
        return finishedTime != null;
    }
}
