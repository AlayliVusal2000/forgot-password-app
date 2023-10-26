package az.forgotpasswordapp.request;

import az.forgotpasswordapp.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private UserRole userRole;

}