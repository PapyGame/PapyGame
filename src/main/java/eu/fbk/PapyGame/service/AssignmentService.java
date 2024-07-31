package eu.fbk.PapyGame.service;

import eu.fbk.PapyGame.model.Assignment;
import eu.fbk.PapyGame.repository.AssignmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public void createAssignment(String project_id, String assignment_text) {
        Assignment assignment = new Assignment();
        assignment.setProjectId(project_id);
        assignment.setAssignmentText(assignment_text);
        assignmentRepository.save(assignment);
    }

    public Assignment getAssignmentById(String id) {
        return assignmentRepository.findById(id).orElse(null);
    }

    public Assignment saveAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Assignment getAssignmentByProjectId(String project_id) {
        return assignmentRepository.findByProjectId(project_id);
    }
}
