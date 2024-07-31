package eu.fbk.PapyGame.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "assignments")
public class Assignment {
    @Id
    private String id;
    private String project_id;
    private String assignment_text;

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return project_id;
    }

    public void setProjectId(String projectId) {
        this.project_id = projectId;
    }

    public String getAssignmentText() {
        return assignment_text;
    }

    public void setAssignmentText(String assignmentText) {
        this.assignment_text = assignmentText;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id='" + id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", assignment_text='" + assignment_text + '\'' +
                '}';
    }
}
