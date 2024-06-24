package com.frostholl.projectHiveBack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class InviteCodeGeneratorConfig {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int INVITE_CODE_LENGTH = 5;


    @Bean
    public RandomStringGenerator inviteCodeGenerator() {
        return new RandomStringGenerator();
    }

    public class RandomStringGenerator {
        private final Random random = new Random();

        public String generateRandomString() {
            StringBuilder sb = new StringBuilder(INVITE_CODE_LENGTH);
            for (int i = 0; i < INVITE_CODE_LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }
            return sb.toString();
        }
    }
}
