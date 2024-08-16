package eu.fbk.PapyGame.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "projects")
@CompoundIndexes({
    @CompoundIndex(name = "unique_project_ctx", def = "{'project_id': 1, 'ctxId': 1}", unique = true)
})
public class Project {
    @Id
    private String id;
    @Indexed(unique = true)
    private String project_id;
    private String representationId;
    private String assignment_id;
    private String ctxId;
    private String graderResults;

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

    public String getRepresentationId() {
        return representationId;
    }

    public void setRepresentationId(String representationId) {
        this.representationId = representationId;
    }

    public String getAssignmentId() {
        return assignment_id;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignment_id = assignmentId;
    }

    public String getCtxId() {
        return ctxId;
    }

    public void setCtxId(String ctxId) {
        this.ctxId = ctxId;
    }

    public String getGraderResults() {
        return graderResults;
    }

    public void setGraderResults(String graderResults) {
        this.graderResults = graderResults;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", representation_id='" + representationId + '\'' +
                ", assignment_id='" + assignment_id + '\'' +
                ", ctxId='" + ctxId + '\'' +
                ", graderResults='" + graderResults + '\'' +
                '}';
    }
}
