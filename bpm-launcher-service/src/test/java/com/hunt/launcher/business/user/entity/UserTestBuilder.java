package com.hunt.bpm-launcher-service.business.user.entity;

import com.hunt.bpm-launcher-service.DatabaseTestBuilder;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Repository;

@Repository
public class UserTestBuilder extends DatabaseTestBuilder<User, Long> {

    public static final String USERNAME_1 = "user1";
    public static final String PASSWORD_1 = "password1";
    public static final Long EXPIRATION_TIME_IN_MINUTES = 60L;

    @Override
    protected User createFull() {
        User user = new User();
        user.setUsername(USERNAME_1);
        user.setPassword(DigestUtils.sha512Hex(PASSWORD_1));
        user.setExpirationTimeInMinutes(EXPIRATION_TIME_IN_MINUTES);

        return user;
    }
}
