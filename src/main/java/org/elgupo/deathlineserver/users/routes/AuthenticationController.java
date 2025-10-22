package org.elgupo.deathlineserver.users.routes;

import org.elgupo.deathlineserver.users.AuthException;
import org.elgupo.deathlineserver.users.models.AuthRequest;
import org.elgupo.deathlineserver.users.models.AuthResponse;
import org.elgupo.deathlineserver.users.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authenticationService.register(authRequest));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) throws AuthException {
        return ResponseEntity.ok(authenticationService.login(authRequest));
    }

}
