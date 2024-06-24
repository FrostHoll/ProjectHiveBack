package com.frostholl.projectHiveBack.repository;

import com.frostholl.projectHiveBack.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findTeamById(Integer id);
}
