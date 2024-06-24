package com.frostholl.projectHiveBack.util;

import com.frostholl.projectHiveBack.service.InviteCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpiredInviteCodeDeleter {
    private final InviteCodeService service;

    @Scheduled(fixedRate = 3_600_000L)
    public void deleteExpiredInviteCodes() {
        service.deleteExpiredCodes();
    }
}
