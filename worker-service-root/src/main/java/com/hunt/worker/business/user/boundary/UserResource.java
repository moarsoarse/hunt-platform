package com.hunt.worker-service-root.business.user.boundary;

import java.util.Optional;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import com.hunt.worker-service-root.business.user.control.Authenticator;
import com.hunt.worker-service-root.business.user.control.TokenIssuer;
import com.hunt.worker-service-root.business.user.control.TokenValidator;
import com.hunt.worker-service-root.business.user.control.UserRepository;
import com.hunt.worker-service-root.business.user.entity.TokenNotValidException;
import com.hunt.worker-service-root.business.user.entity.User;
import com.hunt.worker-service-root.business.user.entity.UsernameAndPassword;
import com.hunt.worker-service-root.business.user.entity.ValidatedToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value ="/api/user", description = "User resource prepared to login users to the system.")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserResource {

    @Autowired
    private TokenIssuer tokenIssuer;

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private TokenValidator tokenValidator;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation("Login/authenticate user with provided username and password to the system.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "When an user has been successfully logged/authenticated to the system."),
            @ApiResponse(code = 401, message = "When unable to authenticate (wrong username or password).")
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UsernameAndPassword usernameAndPassword) {
        User authenticatedUser = authenticator.authenticate(usernameAndPassword);

        String token = tokenIssuer.issueToken(authenticatedUser);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
    }

    @ApiOperation("Returns information about logged user by given token.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "When given token is valid and user exists."),
            @ApiResponse(code = 404, message = "When unable to find an user by given token."),
            @ApiResponse(code = 400, message = "When given token is invalid.")
    })
    @PostMapping("/info")
    public ResponseEntity info(@RequestBody String token) {
        try {
            ValidatedToken validatedToken = tokenValidator.validate(token);

            Optional<User> user = userRepository.findByUsername(validatedToken.getSubject());

            return user.<ResponseEntity>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (TokenNotValidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
