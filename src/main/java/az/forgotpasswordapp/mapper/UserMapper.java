package az.forgotpasswordapp.mapper;

import az.forgotpasswordapp.entity.User;
import az.forgotpasswordapp.request.UserRequest;
import az.forgotpasswordapp.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User fromUserSignUpRequestToModel(UserRequest userRequest);


    UserResponse fromModelToResponse(User user);

}