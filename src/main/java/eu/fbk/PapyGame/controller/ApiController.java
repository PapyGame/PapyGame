package eu.fbk.PapyGame.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import eu.fbk.PapyGame.service.jdd.JsonDiffCheckerService;
import eu.fbk.PapyGame.service.JsonFormatterService;
import eu.fbk.PapyGame.model.Assignment;
import eu.fbk.PapyGame.model.Project;
import eu.fbk.PapyGame.service.AssignmentService;
// import eu.fbk.PapyGame.service.JsonComparisonService;
import eu.fbk.PapyGame.service.PostgreSqlService;
import eu.fbk.PapyGame.service.ProjectService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class ApiController {
    // @Autowired
    // private JsonDiffCheckerService jsonDiffCheckerService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private ProjectService projectService;
    // @Autowired
    // private JsonComparisonService jsonComparisonService;
    @Autowired
    private JsonFormatterService jsonFormatterService;
    @Autowired
    private PostgreSqlService postgreSqlService;
    
    @GetMapping("/assignment")
    public String getAssignment(@RequestParam String project_id) {
        Project project = projectService.getProjectByProjectId(project_id);
        if (project == null)
            return "No assignment related to this project";
        else {
            Assignment assignment = assignmentService.getAssignmentByProjectId(project.getAssignmentId());
            if (assignment != null)
                return assignment.getAssignmentText(); 
            else 
                return "Something went wrong";
        }
    }

    // @GetMapping("/constraints")
    // public ResponseEntity<Map<String, Integer>> getConstraints(@RequestParam(name = "user") String user) {
    //     String projectId;
    //     switch (user) {
    //         // case "student":
    //         //     projectId = "3654b5d7-f8cb-4f4f-a38b-018816bdc06e";
    //         //     break;
    //         case "teacher":
    //             projectId = "6c3a0179-eda2-4bac-a05d-906d437433f7";
    //             break;
    //         default:
    //             projectId = user;
    //             // throw new IllegalArgumentException("Invalid user type");
    //     }

    //     String content = postgreSqlService.getContentByProjectId(projectId);
    //     String formattedJson = jsonFormatterService.format(content);

    //     try {
    //         ObjectMapper objectMapper = new ObjectMapper();
    //         JsonNode jsonNode = objectMapper.readTree(formattedJson);

    //         return ResponseEntity.ok(jsonComparisonService.getConstraints(jsonNode));
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    //     }
    // }

    @PostMapping("/createAssignment")
    public ResponseEntity<String> assignmentCreation(@RequestBody Map<String, String> requestBody) {
        String projectId = requestBody.get("project_id");
        String assignmentText = requestBody.get("assignment_text");
        String project = postgreSqlService.getDocumentIdByProjectId(projectId);
        if (project == null) {
            return ResponseEntity.badRequest().body("No project related to this project_id");
        }
        assignmentService.createAssignment(projectId, assignmentText);
        return ResponseEntity.ok("Assignment created successfully");
    }
    
    @GetMapping("/assignmentProjects")
    public ResponseEntity<List<Assignment>> getAssignmentProjects() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/graderResults")
    public ResponseEntity<String> getGraderResults(@RequestParam("project_id") String attemptProjectId) throws Exception {
        Project attemptProject = projectService.getProjectByProjectId(attemptProjectId);
        if (attemptProject == null) {
            return ResponseEntity.badRequest().body("No project related to this project_id");
        }

        String attemptDocumentId = postgreSqlService.getDocumentIdByProjectId(attemptProjectId);
        String solutionProjectId = attemptProject.getAssignmentId();
        String solutionDocumentId = postgreSqlService.getDocumentIdByProjectId(solutionProjectId);

        RestTemplate restTemplate = new RestTemplate();
                
        String url = "http://localhost:8080/api/editingcontexts/";

        ClassPathResource classPathResource = new ClassPathResource("ClassdiagramGrader-jar-with-dependencies.jar");
        Path tempJarPath = Files.createTempFile("grader-" + attemptProjectId + "-" + solutionProjectId, ".jar");
        Files.copy(classPathResource.getInputStream(), tempJarPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        
        Path attemptFilePath = Files.createTempFile("attempt" + attemptProjectId, ".uml");
        File attemptFile = attemptFilePath.toFile();
        Path solutionFilePath = Files.createTempFile("solution" + solutionProjectId, ".uml");
        File solutionFile = solutionFilePath.toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(attemptFile))) {
            writer.write(restTemplate.getForObject(url + attemptProjectId + "/documents/" + attemptDocumentId, String.class));
            writer.flush();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(solutionFile))) {
            writer.write(restTemplate.getForObject(url + solutionProjectId + "/documents/" + solutionDocumentId, String.class));
            writer.flush();
        }

        StringBuilder results = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", tempJarPath.toString(), "-jsonOutput", "-a", attemptFilePath.toString(), "-s", solutionFilePath.toString());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    results.append(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return ResponseEntity.status(500).body("Process exited with code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error" + e.getMessage());
        }

        Files.deleteIfExists(attemptFilePath);
        Files.deleteIfExists(solutionFilePath);
        Files.deleteIfExists(tempJarPath);

        attemptProject.setGraderResults(results.toString());
        projectService.saveProject(attemptProject);

        return ResponseEntity.ok(jsonFormatterService.format(results.toString()));
    }

    @GetMapping("/existingGrade")
    public ResponseEntity<String> getExistingGrade(@RequestParam("project_id") String projectId) {
        Project project = projectService.getProjectByProjectId(projectId);
        if (project == null) {
            return ResponseEntity.badRequest().body("No project related to this project_id");
        }
        String previousResults = project.getGraderResults();
        if (previousResults == null || previousResults.isEmpty()) {
            return ResponseEntity.ok().body("{}");
        } else {
            return ResponseEntity.ok(jsonFormatterService.format(previousResults));
        }
    }
    
    @PostMapping("/newBlankProject")
    public ResponseEntity<String> newBlankProject(@RequestBody Map<String, String> requestBody) throws Exception {
        String ctxId = (String) requestBody.get("ctxId");
        String assignment_id = (String) requestBody.get("assignment_id");
        String nomeUtente = (String) requestBody.get("nomeUtente");
        String projectName = nomeUtente + ".uml";

        if (assignmentService.getAssignmentByProjectId(assignment_id) == null) {
            return ResponseEntity.badRequest().body("No assignment related to this project_id");
        }

        List<Project> projects = projectService.getProjectByCtxId(ctxId);

        if (projects.size() > 0) {
            for (Project project : projects) {
                if (project.getAssignmentId().equals(assignment_id)) {
                    if (!project.getGraderResults().isEmpty()) {
                        project.setGraderResults("");
                        projectService.saveProject(project);
                    }
                    return ResponseEntity.ok("https://papygame.tech/projects/"+ project.getProjectId() + "/edit/" + project.getRepresentationId() + "?ctxId=" + ctxId);
                }
            }
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request;
        String query;
        String url = "http://localhost:8080/api/graphql";
        ResponseEntity<String> response;
        JSONObject jsonResponse;

        String id = UUID.randomUUID().toString();

        query = "{ \"operationName\": \"createProject\", \"variables\": { \"input\": { \"id\": \"" + id + "\", \"name\": \"" + projectName + "\", \"natures\": [] } }, \"query\": \"mutation createProject($input: CreateProjectInput!) { createProject(input: $input) { __typename ... on CreateProjectSuccessPayload { project { id __typename } __typename } ... on ErrorPayload { message __typename } } }\" }";
        request = new HttpEntity<>(query, headers);
        response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        jsonResponse = new JSONObject(response.getBody());
        String projectId = jsonResponse.getJSONObject("data")
                                       .getJSONObject("createProject")
                                       .getJSONObject("project")
                                       .getString("id");

        query = "{ \"operationName\": \"getStereotypeDescriptions\", \"variables\": { \"editingContextId\": \"" + projectId + "\" }, \"query\": \"query getStereotypeDescriptions($editingContextId: ID!) { viewer { editingContext(editingContextId: $editingContextId) { stereotypeDescriptions { edges { node { id label __typename } __typename } __typename } __typename } __typename } }\" }";
        request = new HttpEntity<>(query, headers);
        response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        jsonResponse = new JSONObject(response.getBody());
        JSONArray edges = jsonResponse.getJSONObject("data")
                                      .getJSONObject("viewer")
                                      .getJSONObject("editingContext")
                                      .getJSONObject("stereotypeDescriptions")
                                      .getJSONArray("edges");

        String stereotypeDescriptionId = "";
        for (int i = 0; i < edges.length(); i++) {
            JSONObject node = edges.getJSONObject(i).getJSONObject("node");
            if (node.getString("label").equals("UML.uml")) {
                stereotypeDescriptionId = node.getString("id");
                break;
            }
        }

        String documentId = UUID.randomUUID().toString();
        query = "{ \"operationName\": \"createDocument\", \"variables\": { \"input\": { \"id\": \"" + documentId + "\", \"editingContextId\": \"" + projectId + "\", \"name\": \"" + projectName + "\", \"stereotypeDescriptionId\": \"" + stereotypeDescriptionId + "\" } }, \"query\": \"mutation createDocument($input: CreateDocumentInput!) { createDocument(input: $input) { __typename ... on ErrorPayload { message __typename } } }\" }";
        request = new HttpEntity<>(query, headers);
        response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        String content = postgreSqlService.getContentByProjectId(projectId);

        JSONObject jsonObject = new JSONObject(content);
        JSONArray contentArray = jsonObject.getJSONArray("content");
        String idModel = contentArray.getJSONObject(0).getString("id");

        query = "{\"operationName\":\"getRepresentationDescriptions\",\"variables\":{\"editingContextId\":\"" + projectId + "\",\"objectId\":\"" + idModel + "\"},\"query\":\"query getRepresentationDescriptions($editingContextId: ID!, $objectId: ID!) { viewer { editingContext(editingContextId: $editingContextId) { representationDescriptions(objectId: $objectId) { edges { node { id label defaultName __typename } __typename } pageInfo { hasNextPage hasPreviousPage startCursor endCursor __typename } __typename } __typename } __typename } }\"}";
        request = new HttpEntity<>(query, headers);
        response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        jsonResponse = new JSONObject(response.getBody());
        edges = jsonResponse.getJSONObject("data")
                            .getJSONObject("viewer")
                            .getJSONObject("editingContext")
                            .getJSONObject("representationDescriptions")
                            .getJSONArray("edges");

        String representationDescriptionId = "";
        for (int i = 0; i < edges.length(); i++) {
            JSONObject node = edges.getJSONObject(i).getJSONObject("node");
            if (node.getString("label").equals("Class Diagram")) {
                representationDescriptionId = node.getString("id");
                break;
            }
        }

        String classDiagramId = UUID.randomUUID().toString();
        query = "{\"operationName\":\"createRepresentation\",\"variables\":{\"input\":{\"id\":\"" + classDiagramId + "\",\"editingContextId\":\"" + projectId + "\",\"objectId\":\"" + idModel + "\",\"representationDescriptionId\":\"" + representationDescriptionId + "\",\"representationName\":\"" + projectName + "\"}},\"query\":\"mutation createRepresentation($input: CreateRepresentationInput!) {createRepresentation(input: $input) {__typename ... on CreateRepresentationSuccessPayload {representation {id label kind __typename} __typename} ... on ErrorPayload {message __typename}}}\"}";
        request = new HttpEntity<>(query, headers);
        response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        jsonResponse = new JSONObject(response.getBody());
        String representationId = jsonResponse.getJSONObject("data")
                                    .getJSONObject("createRepresentation")
                                    .getJSONObject("representation")
                                    .getString("id");

        projectService.createProject(projectId, representationId, assignment_id, ctxId);

        return ResponseEntity.ok("https://papygame.tech/projects/"+ projectId + "/edit/" + representationId + "?ctxId=" + ctxId);
    }
}

