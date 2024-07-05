package com.frostholl.projectHiveBack.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "invite_code")
public class InviteCode {
    @Id
    private String id;

    @JsonBackReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    private Date expireDate;

    public boolean isExpired() {
        return expireDate.before(new Date(System.currentTimeMillis()));
    }
}
