package eu.fbk.PapyGame.service;

import eu.fbk.PapyGame.model.Project;
// import eu.fbk.PapyGame.repository.ProjectRepository;
import eu.fbk.PapyGame.repository.ProjectRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public void createProject(String project_id, String representation_id, String assignment_id, String ctxId) {
        Project project = new Project();
        project.setProjectId(project_id);
        project.setRepresentationId(representation_id);
        project.setAssignmentId(assignment_id);
        project.setCtxId(ctxId);
        project.setGraderResults("");
        projectRepository.save(project);
    }

    public Project getProjectById(String id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public Project getProjectByProjectId(String project_id) {
        return projectRepository.findByProjectId(project_id);
    }

    public List<Project> getProjectByCtxId(String ctxId) {
        return projectRepository.findByCtxId(ctxId);
    }
}
