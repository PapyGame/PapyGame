package eu.fbk.PapyGame.service;

import eu.fbk.PapyGame.model.Project;
// import eu.fbk.PapyGame.repository.ProjectRepository;
import eu.fbk.PapyGame.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public void createProject(String project_id, String assignment_id) {
        Project project = new Project();
        project.setProjectId(project_id);
        project.setAssignmentId(assignment_id);
        projectRepository.save(project);
    }

    public Project getProjectById(String id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project saveAssignment(Project assignment) {
        return projectRepository.save(assignment);
    }

    public Project getAssignmentByProjectId(String project_id) {
        return projectRepository.findByProjectId(project_id);
    }
}
