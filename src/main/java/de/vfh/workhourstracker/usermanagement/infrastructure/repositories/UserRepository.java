package de.vfh.workhourstracker.usermanagement.infrastructure.repositories;

import de.vfh.workhourstracker.usermanagement.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    void deleteById(Long id);
}
