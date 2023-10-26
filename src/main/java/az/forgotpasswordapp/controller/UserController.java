package az.forgotpasswordapp.controller;

import az.forgotpasswordapp.entity.User;
import az.forgotpasswordapp.exception.UserNotFoundException;
import az.forgotpasswordapp.exception.error.ErrorMessage;
import az.forgotpasswordapp.repo.UserRepository;
import az.forgotpasswordapp.request.ForgotPasswordRequest;
import az.forgotpasswordapp.request.LoginRequest;
import az.forgotpasswordapp.request.UserRequest;
import az.forgotpasswordapp.response.AuthenticationResponse;
import az.forgotpasswordapp.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserRequest userRequest) {
        return userService.signUp(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String jwt = userService.login(loginRequest);
        if (jwt == null) {
            return ResponseEntity.status(BAD_REQUEST).build();
        } else {
            User user = userRepository.findByEmailEqualsIgnoreCase(loginRequest.getEmail()).orElseThrow(
                    () -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND));
            if (Objects.nonNull(user)) {
                AuthenticationResponse response = new AuthenticationResponse();
                response.setJwtToken(jwt);
                response.setId(user.getId());
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.USER_NOT_FOUND);
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        return userService.forgotPassword(forgotPasswordRequest);
    }

}