package engine.users.controller;

import engine.users.model.RegistrationRequest;
import engine.users.model.entity.QuizUser;
import engine.users.repository.QuizUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final LOG = LoggerFactory.getLogger(UserController.class);

    private final QuizUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserController(QuizUserRepository repository,
                          PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest registrationRequest) {

        LOG.info("Trying to register new user with email: {}", registrationRequest.getEmail());

        if (repository.existsByEmail(registrationRequest.getEmail())) {
            LOG.warn("User with email {} already exists", registrationRequest.getEmail());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var user = new QuizUser();
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        repository.save(user);

        LOG.info("Successfully created user with email: {}", registrationRequest.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
