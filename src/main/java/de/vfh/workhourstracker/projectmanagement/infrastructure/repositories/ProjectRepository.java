package de.vfh.workhourstracker.projectmanagement.infrastructure.repositories;

import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project save(Project project);

    Optional<Project> findById(Long id);

    List<Project> findAll();

    void deleteById(Long id);

    List<Project> findProjectByUserId(Long userId);

    @Query("SELECT p FROM Project p WHERE p.userId = :userId AND p.id = :projId")
    List<Project> findProjectByUserIdAndProjId(@Param("userId") Long userId, @Param("projId") Long projId);
}
