package com.hunt.worker-service-root.business.user.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import com.hunt.worker-service-root.business.user.control.TokenValidator;
import com.hunt.worker-service-root.business.user.entity.TokenNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value ="/api/token", description = "Token resource prepared to operate on the JWT token.")
@RestController
@RequestMapping("/api/token")
@Slf4j
public class TokenResource {

    @Autowired
    private TokenValidator tokenValidator;

    @ApiOperation("Validates given token, checks if token is valid and can be used to perform requests.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "When given token is valid."),
            @ApiResponse(code = 401, message = "When given token is invalid.")
    })
    @PostMapping("/validate")
    public ResponseEntity validate(@RequestBody String token) {
        try {
            tokenValidator.validate(token);
            return ResponseEntity.ok().build();
        } catch (TokenNotValidException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
