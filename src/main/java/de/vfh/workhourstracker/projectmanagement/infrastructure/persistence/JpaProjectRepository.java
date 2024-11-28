package de.vfh.workhourstracker.projectmanagement.infrastructure.persistence;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProjectRepository extends JpaRepository<Project, Long>, ProjectRepository {
}
