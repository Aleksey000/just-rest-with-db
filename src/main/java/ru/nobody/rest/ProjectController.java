package ru.nobody.rest;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.nobody.auth.services.AuthenticationService;
import ru.nobody.data.entity.Project;
import ru.nobody.data.entity.User;
import ru.nobody.services.ProjectService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// TODO логи
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/list")
    public List<Project> list() {
        User user = AuthenticationService.getUser();

        return projectService.findByUser(user);
    }

    @GetMapping("/{projectId}")
    public Project getProject(@PathVariable("projectId") UUID projectId) {
        User user = AuthenticationService.getUser();

        return projectService.findByUser(projectId, user).orElse(new Project());
    }

    @PostMapping("/create")
    public Project createProject(@RequestBody Map body) {
        if (body == null || StringUtils.isBlank((String) body.get("projectName"))) {
            throw new RuntimeException("projectName is blank");
        }
        User user = AuthenticationService.getUser();
        String projectName = (String) body.get("projectName");
        return projectService.createProject(projectName, user);
    }

    @DeleteMapping("/{projectId}")
    public String deleteProject(@PathVariable("projectId") UUID projectId) {
        User user = AuthenticationService.getUser();
        projectService.deleteProject(user, projectId);

        return "OK";
    }
}
