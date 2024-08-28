package eu.fbk.PapyGame.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "assignments")
public class Assignment {
    @Id
    private String id;
    @Indexed(unique = true)
    private String project_id;
    private String assignment_title;
    private String assignment_text;
    private List<Map<String, String>> tags;

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

    public String getAssignmentTitle() {
        return assignment_title;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignment_title = assignmentTitle;
    }

    public String getAssignmentText() {
        return assignment_text;
    }

    public void setAssignmentText(String assignmentText) {
        this.assignment_text = assignmentText;
    }

    public List<Map<String, String>> getTags() {
        return tags;
    }

    public void setTags(List<Map<String, String>> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id='" + id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", assignment_title='" + assignment_title + '\'' +
                ", assignment_text='" + assignment_text + '\'' +
                ", tags=" + tags +
                '}';
    }
}
