package de.vfh.workhourstracker.usermanagement.infrastructure.repositories;

import de.vfh.workhourstracker.usermanagement.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
