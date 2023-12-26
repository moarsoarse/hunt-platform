package com.hunt.worker-service-root.business.user.control;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hunt.worker-service-root.BaseIntegrationTest;
import com.hunt.worker-service-root.TestBuilderConfiguration;
import com.hunt.worker-service-root.business.user.entity.User;
import com.hunt.worker-service-root.business.user.entity.UserTestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryIT extends BaseIntegrationTest {

    @Autowired
    private UserTestBuilder userTestBuilder;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsernameAndPasswordGivenNotExistingUsername() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);
        String username = "some username";
        String password = DigestUtils.sha512Hex(UserTestBuilder.PASSWORD_1);

        //WHEN
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);

        //THEN
        assertFalse(user.isPresent());
    }

    @Test
    public void findByUsernameAndPasswordGivenNotExistingPassword() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);
        String username = UserTestBuilder.USERNAME_1;
        String password = DigestUtils.sha512Hex("some password");

        //WHEN
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);

        //THEN
        assertFalse(user.isPresent());
    }

    @Test
    public void findByUsernameAndPasswordGivenCorrectUsernameAndPassword() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);

        String username = UserTestBuilder.USERNAME_1;
        String password = DigestUtils.sha512Hex(UserTestBuilder.PASSWORD_1);

        //WHEN
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);

        //THEN
        assertTrue(user.isPresent());
        assertEquals(username, user.get().getUsername());
        assertEquals(password, user.get().getPassword());
    }

    @Test
    public void findByUsernameGivenNotExistingUsername() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);
        String username = "some username";

        //WHEN
        Optional<User> user = userRepository.findByUsername(username);

        //THEN
        assertFalse(user.isPresent());
    }

    @Test
    public void findByUsernameGivenCorrectUsername() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);

        String username = UserTestBuilder.USERNAME_1;

        //WHEN
        Optional<User> user = userRepository.findByUsername(username);

        //THEN
        assertTrue(user.isPresent());
        assertEquals(username, user.get().getUsername());
    }
}