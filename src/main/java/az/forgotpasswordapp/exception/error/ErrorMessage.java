package az.forgotpasswordapp.exception.error;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorMessage {

    public static final String USER_NOT_FOUND = "User not found. ";
    public static final String INVALID_DATA = "Invalid data. ";
    public static final String BAD_CREDENTIALS = "Bad credentials. ";
    public static final String CHECK_EMAIL = "Check your mail for Credentials. ";
    public static final String BY_YOUR_APP = "Credentials by OfiCenter. ";

}