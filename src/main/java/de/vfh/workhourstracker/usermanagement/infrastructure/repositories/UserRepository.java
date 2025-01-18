package de.vfh.workhourstracker.usermanagement.infrastructure.repositories;

import de.vfh.workhourstracker.usermanagement.domain.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User save(User user);
}
