package ru.nobody.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nobody.data.entity.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query("select p FROM Project as p LEFT JOIN ProjectMember pm ON p.id = pm.project.id WHERE pm.user.id = :userId")
    List<Project> findAllByUser(@Param("userId") UUID userId);

    @Query("select p FROM Project as p LEFT JOIN ProjectMember pm ON p.id = pm.project.id WHERE pm.user.id = :userId AND p.id = :id")
    Optional<Project> findByIdAndUser(@Param("id") UUID id, @Param("userId") UUID userId);
}
