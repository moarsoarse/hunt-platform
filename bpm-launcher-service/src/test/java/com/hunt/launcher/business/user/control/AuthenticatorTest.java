package com.hunt.bpm-launcher-service.business.user.control;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hunt.bpm-launcher-service.business.user.entity.AuthenticationException;
import com.hunt.bpm-launcher-service.business.user.entity.User;
import com.hunt.bpm-launcher-service.business.user.entity.UsernameAndPassword;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class AuthenticatorTest {

    private static final String PASSWORD = "password";
    private static final String HASHED_PASSWORD = DigestUtils.sha512Hex(PASSWORD);
    private static final String USERNAME = "username";

    @Mock
    private UsernameAndPassword usernameAndPassword;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @InjectMocks
    private Authenticator authenticator;

    @Test
    public void authenticateWhenNoUserCorrespondsToGivenUsernameAndPassword() {
        //GIVEN
        doReturn(USERNAME).when(usernameAndPassword).getUsername();
        doReturn(PASSWORD).when(usernameAndPassword).getPassword();

        doReturn(Optional.empty()).when(userRepository).findByUsernameAndPassword(USERNAME, HASHED_PASSWORD);

        //WHEN
        assertThrows(AuthenticationException.class, () -> {
            authenticator.authenticate(usernameAndPassword);
        });
    }

    @Test
    public void authenticateWhenFoundUser() {
        //GIVEN
        doReturn(USERNAME).when(usernameAndPassword).getUsername();
        doReturn(PASSWORD).when(usernameAndPassword).getPassword();

        doReturn(Optional.of(user)).when(userRepository).findByUsernameAndPassword(USERNAME, HASHED_PASSWORD);

        //WHEN
        authenticator.authenticate(usernameAndPassword);
    }

}