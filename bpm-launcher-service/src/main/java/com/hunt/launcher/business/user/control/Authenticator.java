package com.hunt.bpm-launcher-service.business.user.control;

import com.hunt.bpm-launcher-service.business.user.entity.AuthenticationException;
import com.hunt.bpm-launcher-service.business.user.entity.User;
import com.hunt.bpm-launcher-service.business.user.entity.UsernameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Slf4j
@Component
public class Authenticator {

    private static final String AUTHENTICATION_ERROR_MESSAGE = "Unable to authenticate user = {0} with given password. Login or password is incorrect.";

    @Autowired
    private UserRepository userRepository;

    public User authenticate(UsernameAndPassword usernameAndPassword) {
        log.info("Authenticating user = {}.", usernameAndPassword.getUsername());

        String hashedPassword = DigestUtils.sha512Hex(usernameAndPassword.getPassword());

        Optional<User> user = userRepository.findByUsernameAndPassword(usernameAndPassword.getUsername(), hashedPassword);

        if (user.isPresent()) {
            log.info("Authentication successful for user = {}.", usernameAndPassword.getUsername());
            return user.get();
        } else {
            log.warn("Unable to authenticate user = {}. Probable wrong login or password!", usernameAndPassword.getUsername());
            throw new AuthenticationException(MessageFormat.format(AUTHENTICATION_ERROR_MESSAGE, usernameAndPassword.getUsername()));
        }
    }
}
