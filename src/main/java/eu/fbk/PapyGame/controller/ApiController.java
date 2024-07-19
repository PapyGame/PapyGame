package eu.fbk.PapyGame.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.fbk.PapyGame.service.jdd.JsonDiffCheckerService;
import eu.fbk.PapyGame.service.JsonFormatterService;
import eu.fbk.PapyGame.model.Assignment;
import eu.fbk.PapyGame.service.AssignmentService;
import eu.fbk.PapyGame.service.JsonComparisonService;
import eu.fbk.PapyGame.service.PostgreSqlService;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class ApiController {
    @Autowired
    private JsonDiffCheckerService jsonDiffCheckerService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private JsonComparisonService jsonComparisonService;
    @Autowired
    private JsonFormatterService jsonFormatterService;
    @Autowired
    private PostgreSqlService postgreSqlService;
    
    @GetMapping("/assignment")
    public String getAssignment(@RequestParam String project_id) {
        Assignment assignment = assignmentService.getAssignmentByProjectId(project_id);
        if (assignment == null)
            return "No assignment related to this project";
        else 
            return assignment.getAssignment_text(); 
    }

    @GetMapping("/prova2")
    public void prova2(@RequestParam String json1, @RequestParam String json2) {
        // json = jsonFormatterService.format(json);
        // jsonMapperService.jsonMapper(json);
        jsonDiffCheckerService.jsonDiffChecker(json1, json2);
    }

    @PostMapping("/compare")
    public void compareJson(@RequestBody String requestBody) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(requestBody);

        // jsonComparisonService.getConstraints(jsonNode.get("teacher"));
        List<String> differences = jsonComparisonService.compareJson(jsonNode.get("teacher"), jsonNode.get("student"));
        for (String string : differences) {
            System.out.println(string);
        }
    }

    @PostMapping("/format")
    public ResponseEntity<String> formatJson(@RequestBody String json) throws Exception {
        return ResponseEntity.ok(jsonFormatterService.format(json));
    }

    @GetMapping("/getFormattedJson")
    public ResponseEntity<String> getFormattedJson(@RequestParam(name = "user") String user) {
        String projectId;
        switch (user) {
            // case "student":
            //     projectId = "3654b5d7-f8cb-4f4f-a38b-018816bdc06e";
            //     break;
            case "teacher":
                projectId = "6c3a0179-eda2-4bac-a05d-906d437433f7";
                break;
            default:
                projectId = user;
                // throw new IllegalArgumentException("Invalid user type");
        }

        String content = postgreSqlService.getContentByProjectId(projectId);

        return ResponseEntity.ok(jsonFormatterService.format(content));
    }

    @GetMapping("/constraints")
    public ResponseEntity<Map<String, Integer>> getConstraints(@RequestParam(name = "user") String user) {
        String projectId;
        switch (user) {
            // case "student":
            //     projectId = "3654b5d7-f8cb-4f4f-a38b-018816bdc06e";
            //     break;
            case "teacher":
                projectId = "6c3a0179-eda2-4bac-a05d-906d437433f7";
                break;
            default:
                projectId = user;
                // throw new IllegalArgumentException("Invalid user type");
        }

        String content = postgreSqlService.getContentByProjectId(projectId);
        String formattedJson = jsonFormatterService.format(content);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(formattedJson);

            return ResponseEntity.ok(jsonComparisonService.getConstraints(jsonNode));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/graderResults")
    public ResponseEntity<String> getGraderResults(@RequestParam(name = "user") String user) {
        String results;

        switch (user) {
            case "41bddc5b-2835-4f2b-a39d-509fe2f2def6":
                results = "Grade: 9.5 out of 11.0\n" +
        "Classes\n" +
        "  - Customer: 2.0 points - Comments: Equivalent to solution class Customer.\n" +
        "  - Account: 2.0 points - Comments: Equivalent to solution class Account.\n" +
        "Attributes\n" +
        "  - Customer.name: 1.0 points - Comments: Equivalent to solution attribute Customer.name.\n" +
        "  - Account.balance: 1.0 points - Comments: Equivalent to solution attribute Account.balance.\n" +
        "Associations\n" +
        "  - Account_Customer: 2.0 points - Comments: Equivalent to solution association Account_Customer.\n" +
        "Association Ends\n" +
        "  - Account.owner: 1.0 points - Comments: Equivalent to solution association end Account.owner.\n" +
        "  - Customer.accounts: 0.5 points - Comments: Equivalent to solution association end Customer.accounts. Wrong lower bound, hence half points.";
                break;
            case "student":
                results = "Grade: 22.0 out of 58.0\r\n" + //
                            "Classes\r\n" + //
                            "  - Guest: 2.0 points - Comments: Equivalent to solution class Person.\r\n" + //
                            "  - Room: 2.0 points - Comments: Equivalent to solution class Room.\r\n" + //
                            "  - Reservation: 2.0 points - Comments: Equivalent to solution class Booking.\r\n" + //
                            "Attributes\r\n" + //
                            "  - Guest.address: 1.0 points - Comments: Equivalent to solution attribute Person.address.\r\n" + //
                            "  - Guest.telephone: 1.0 points - Comments: Equivalent to solution attribute Person.phoneNumber.\r\n" + //
                            "  - Guest.firstname: 1.0 points - Comments: Equivalent to solution attribute Person.name.\r\n" + //
                            "  - Guest.lastname: 0.0 points - Comments: Also equivalent to solution attribute Person.name, hence no points.\r\n" + //
                            "  - Guest.creditcard: 1.0 points - Comments: Equivalent to solution attribute Person.creditcard.\r\n" + //
                            "  - Room.roomnumber: 1.0 points - Comments: Equivalent to solution attribute Room.roomNumber.\r\n" + //
                            "  - Room.bednumber: 0.0 points - Comments: Also equivalent to solution attribute Room.roomNumber, hence no points.\r\n" + //
                            "Associations\r\n" + //
                            "  - Room_Room: 2.0 points - Comments: Equivalent to solution association Room_Room.\r\n" + //
                            "  - Reservation_Room: 2.0 points - Comments: Equivalent to solution association Room_Booking.\r\n" + //
                            "  - Reservation_Guest: 2.0 points - Comments: Equivalent to solution association Person_Booking.\r\n" + //
                            "Association Ends\r\n" + //
                            "  - Room.isAdjacentTo: 0.5 points - Comments: Equivalent to solution association end Room.adjoinedRooms. Wrong upper bound, hence half points.\r\n" + //
                            "  - Reservation.reserved: 0.0 points - Comments: Equivalent to solution association end Booking.bookedRooms. Wrong bounds, hence no points.\r\n" + //
                            "  - Room.reservedBy: 0.5 points - Comments: Equivalent to solution association end Room.book. Wrong upper bound, hence half points.\r\n" + //
                            "  - Reservation.madeBy: 1.0 points - Comments: Equivalent to solution association end Booking.by.\r\n" + //
                            "  - Guest.make: 1.0 points - Comments: Equivalent to solution association end Person.bookings.\r\n" + //
                            "Enumeration Types\r\n" + //
                            "  - SmokingStatus: 1.0 points - Comments: Equivalent to solution enumeration type SmokingStatus.\r\n" + //
                            "  - BedType: 1.0 points - Comments: Equivalent to solution enumeration type BedType.";
                break;
            default:
                results = "NO RESULTS FOUND";
        }
        return ResponseEntity.ok(results);
    }
}

