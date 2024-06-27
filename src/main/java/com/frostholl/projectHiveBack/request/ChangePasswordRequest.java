package com.frostholl.projectHiveBack.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangePasswordRequest {
    private String password;

    private String newPassword;
}
