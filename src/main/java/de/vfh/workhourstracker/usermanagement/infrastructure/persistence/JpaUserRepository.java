package de.vfh.workhourstracker.usermanagement.infrastructure.persistence;

import de.vfh.workhourstracker.usermanagement.domain.user.User;
import de.vfh.workhourstracker.usermanagement.infrastructure.repositories.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository  extends JpaRepository<User, Long>, UserRepository {
}
