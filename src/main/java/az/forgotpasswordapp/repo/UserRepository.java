package az.forgotpasswordapp.repo;

import az.forgotpasswordapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailEqualsIgnoreCase(String email);
    Optional<User> findByUsernameEqualsIgnoreCase(String username);

}