package ru.nobody.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nobody.data.entity.Project;
import ru.nobody.data.entity.User;
import ru.nobody.data.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> findByUser(User user) {
        return projectRepository.findAllByUser(user.getId());
    }

    public Optional<Project> findByUser(UUID projectId, User user) {
        return projectRepository.findByIdAndUser(projectId, user.getId());
    }

    @Transactional
    public Project createProject(String projectName, User user) {
        Project entity = new Project();
        entity.setName(projectName);
        entity.setUsers(Set.of(user));
        projectRepository.save(entity);
        return entity;
    }

    public void deleteProject(User user, UUID projectId) {
        projectRepository.findByIdAndUser(projectId, user.getId()).ifPresent(projectRepository::delete);
    }
}
