package com.frostholl.projectHiveBack.service;

import com.frostholl.projectHiveBack.config.InviteCodeGeneratorConfig;
import com.frostholl.projectHiveBack.exception.team.InviteCodeNotFoundOrExpiredException;
import com.frostholl.projectHiveBack.model.InviteCode;
import com.frostholl.projectHiveBack.model.Team;
import com.frostholl.projectHiveBack.repository.InviteCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InviteCodeService {
    private final InviteCodeRepository repository;

    private final InviteCodeGeneratorConfig.RandomStringGenerator inviteCodeGenerator;

    public InviteCode getInviteCodeById(String id) {
        return repository.findInviteCodeById(id)
                .orElseThrow(() -> new InviteCodeNotFoundOrExpiredException("Invite code was not found or expired."));
    }

    public void deleteExpiredCodes() {
        Date currentDate = new Date(System.currentTimeMillis());
        List<InviteCode> expiredCodes = repository.findExpiredCodes(currentDate);
        repository.deleteAll(expiredCodes);
    }

    public boolean doesTeamHaveValidInviteCode(Team team) {
        if (!doesTeamHaveInviteCode(team))
            return false;
        var inviteCode = getTeamInviteCode(team);
        return !inviteCode.isExpired();
    }

    public boolean doesTeamHaveInviteCode(Team team) {
        return repository
                .findAll().stream().anyMatch(code -> Objects.equals(code.getTeam().getId(), team.getId()));
    }

    public InviteCode getTeamInviteCode(Team team) {
        var inviteCode = repository
                .findAll()
                .stream()
                .filter(code -> Objects.equals(code.getTeam().getId(), team.getId()))
                .findAny();
        return inviteCode
                .orElseThrow(() -> new InviteCodeNotFoundOrExpiredException("Invite code was not found or expired."));
    }

    public void deleteInviteCode(String id) {
        var inviteCode = repository
                .findAll()
                .stream()
                .filter(code -> Objects.equals(code.getId(), id))
                .findAny()
                .orElseThrow(() -> new InviteCodeNotFoundOrExpiredException("Invite code was not found or expired."));
        repository.delete(inviteCode);
    }

    public void deleteInviteCode(InviteCode inviteCode) {
        repository.delete(inviteCode);
    }

    public InviteCode addNewInviteCode(Team team) {
        if (doesTeamHaveInviteCode(team)) {
            var inviteCode = getTeamInviteCode(team);
            deleteInviteCode(inviteCode);
        }

        String id = "";
        do {
            id = inviteCodeGenerator.generateRandomString();
        } while (repository.existsById(id));

        var inviteCode = InviteCode.builder()
                .id(id)
                .team(team)
                .expireDate(new Date(System.currentTimeMillis() + 604_800_000L)) // 7 days expiration time
                .build();
        return repository.save(inviteCode);
    }
}
