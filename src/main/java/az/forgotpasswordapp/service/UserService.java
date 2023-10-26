package az.forgotpasswordapp.service;

import az.forgotpasswordapp.entity.User;
import az.forgotpasswordapp.enums.UserRole;
import az.forgotpasswordapp.exception.error.ErrorMessage;
import az.forgotpasswordapp.mapper.UserMapper;
import az.forgotpasswordapp.repo.UserRepository;
import az.forgotpasswordapp.request.ForgotPasswordRequest;
import az.forgotpasswordapp.request.LoginRequest;
import az.forgotpasswordapp.request.UserRequest;
import az.forgotpasswordapp.seccurity.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final EmailService emailService;

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

    public ResponseEntity<String> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        Optional<User> user = userRepository.findByEmailEqualsIgnoreCase(forgotPasswordRequest.getEmail());
        if (user.isPresent()) {
            emailService.forgetMail(user.get().getEmail(), ErrorMessage.BY_YOUR_APP, user.get().getPassword());
            return ResponseEntity.status(OK).body(ErrorMessage.CHECK_EMAIL);
        } else
            return ResponseEntity.status(BAD_REQUEST).body(ErrorMessage.USER_NOT_FOUND);
    }

    private boolean validationSignUp(UserRequest userRequest) {
        return userRequest.getName() != null && userRequest.getEmail() != null
                && userRequest.getUsername() != null && userRequest.getPassword() != null;
    }
}