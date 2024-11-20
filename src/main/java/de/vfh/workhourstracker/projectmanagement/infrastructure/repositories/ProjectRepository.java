package de.vfh.workhourstracker.projectmanagement.infrastructure.repositories;

import de.vfh.workhourstracker.projectmanagement.domain.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
