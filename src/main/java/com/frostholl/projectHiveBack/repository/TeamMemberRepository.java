package com.frostholl.projectHiveBack.repository;

import com.frostholl.projectHiveBack.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Integer> {
    Optional<TeamMember> findTeamMemberById(Integer id);
}
