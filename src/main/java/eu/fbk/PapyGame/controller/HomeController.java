package eu.fbk.PapyGame.controller;

// import org.apache.logging.log4j.util.PropertySource.Comparator;
// import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.DataConverter;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.oauth2.core.oidc.user.OidcUser;
// import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// import eu.fbk.PapyGame.service.AssignmentService;
// import eu.fbk.PapyGame.service.discoverer.AdvancedDiscovererService;

// import java.util.List;
// import eu.fbk.PapyGame.model.Assignment;

/**
 * Controller for the home page.
 */
@RestController
public class HomeController {

    // @Autowired
    // private AssignmentService assignmentService;
    // @Autowired
    // private ComparatorController comparatorService;
    // @Autowired
    // private AdvancedDiscovererService controller;

    // @GetMapping("/")
    // public String home(Model model, @AuthenticationPrincipal OidcUser principal) {
    //     if (principal != null) {
    //         List<Assignment> assignments = assignmentService.getAllAssignments();
    //         model.addAttribute("assignments", assignments);
    //     }
    //     return "index";
    // }


    @GetMapping("/")
    public String home(Model model) {
        //double similarity = comparatorService.calculateSimilarity("./src/main/resources/universita.ecore", "./src/main/resources/universita.ecore");
        
        //System.out.println("\n\n\n" + similarity + "\n\n\n");
        return "index";
    }
}
