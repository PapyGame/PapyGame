package eu.fbk.PapyGame.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "projects")
public class Project {
    @Id
    private String id;
    @Indexed(unique = true)
    private String project_id;
    private String assignment_id;

    // Getters and setters

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return project_id;
    }

    public void setProjectId(String projectId) {
        this.project_id = projectId;
    }

    public String getAssignmentId() {
        return assignment_id;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignment_id = assignmentId;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", assignment_id='" + assignment_id + '\'' +
                '}';
    }
}
