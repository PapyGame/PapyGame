package eu.fbk.PapyGame.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "assignments")
public class Assignment {
    @Id
    private String id;
    private String project_id;
    private String assignment_text;
    private String assignment_solution;

    public String getId() {
        return id;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getAssignment_text() {
        return assignment_text;
    }

    // public String getAssignment_solution() {
    //     return assignment_solution;
    // }

    @Override
    public String toString() {
        return "Assignment{" +
                "id='" + id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", assignment_text='" + assignment_text + '\'' +
                '}';
    }
}
