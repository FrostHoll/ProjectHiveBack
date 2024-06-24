package com.frostholl.projectHiveBack.repository;

import com.frostholl.projectHiveBack.model.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface InviteCodeRepository extends JpaRepository<InviteCode, String> {
    Optional<InviteCode> findInviteCodeById(String id);

    @Query("SELECT ic FROM InviteCode ic WHERE ic.expireDate <= :currentDate")
    List<InviteCode> findExpiredCodes(@Param("currentDate") Date currentDate);
}
