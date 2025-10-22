package org.elgupo.deathlineserver.users.services;

import lombok.extern.slf4j.Slf4j;
import org.elgupo.deathlineserver.users.AuthException;
import org.elgupo.deathlineserver.users.models.AuthRequest;
import org.elgupo.deathlineserver.users.models.AuthResponse;
import org.elgupo.deathlineserver.users.repositories.UserEntity;
import org.elgupo.deathlineserver.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(AuthRequest authRequest) {
        UserEntity user = userRepository.save(new UserEntity(
                authRequest.email,
                authRequest.password
        ));
        log.info("User registered: {}", user.getEmail());

        return new AuthResponse(
                user.getId(),
                "OK"
        );
    }

    public AuthResponse login(AuthRequest authRequest) throws AuthException {
        UserEntity user = userRepository.findByEmail(authRequest.email);
        if (user == null) {
            throw new AuthException();
        }

        if (user.getPassword().equals(authRequest.password)) {
            log.info("Login successful");
            return new AuthResponse(
                    user.getId(),
                    "OK"
            );
        }
        throw new AuthException();
    }
}
