package az.forgotpasswordapp.service;

import az.forgotpasswordapp.entity.User;
import az.forgotpasswordapp.enums.UserRole;
import az.forgotpasswordapp.exception.UserNotFoundException;
import az.forgotpasswordapp.exception.error.ErrorMessage;
import az.forgotpasswordapp.mapper.UserMapper;
import az.forgotpasswordapp.repo.UserRepository;
import az.forgotpasswordapp.request.LoginRequest;
import az.forgotpasswordapp.request.UserRequest;
import az.forgotpasswordapp.response.UserResponse;
import az.forgotpasswordapp.seccurity.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static az.forgotpasswordapp.constant.SecurityConstant.USER_ALREADY_EXISTS;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public ResponseEntity<?> signUp(UserRequest userSignUpRequest) {
        if (validationSignUp(userSignUpRequest)) {
            Optional<User> user = userRepository.findByEmailEqualsIgnoreCase(userSignUpRequest.getEmail());
            if (user.isEmpty()) {
                User saved = userMapper.fromUserSignUpRequestToModel(userSignUpRequest);
                saved.setUserRole(UserRole.ADMIN);
                return ResponseEntity.status(CREATED)
                        .body(userRepository.save(saved));
            } else {
                log.error("userSignUpRequest {}", userSignUpRequest);
                return ResponseEntity.status(BAD_REQUEST).body(USER_ALREADY_EXISTS);
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    public String login(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmailEqualsIgnoreCase(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            return jwtUtil.generateTokenTest(loginRequest.getEmail());
        }
        log.error("login {}", optionalUser);
        return ErrorMessage.BAD_CREDENTIALS;
    }

    private boolean validationSignUp(UserRequest userRequest) {
        return userRequest.getName() != null && userRequest.getEmail() != null
                && userRequest.getUsername() != null && userRequest.getPassword() != null;
    }



}