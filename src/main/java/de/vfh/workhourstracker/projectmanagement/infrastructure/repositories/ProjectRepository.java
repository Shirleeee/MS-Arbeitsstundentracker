package de.vfh.workhourstracker.projectmanagement.infrastructure.repositories;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project save(Project project);

    Optional<Project> findById(Long id);
    List<Project> findAll();
    void deleteById(Long id);


}
