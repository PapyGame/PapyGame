package eu.fbk.PapyGame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import eu.fbk.PapyGame.service.ComparatorService;

@SpringBootApplication
public class PapyGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(PapyGameApplication.class, args);
		//System.out.println(ComparatorService.calculateSimilarity2("GPTMutators/ASPLE/Asple.ecore", "GPTMutators/ASPLE/mutant1.xml", "GPTMutators/ASPLE/mutant2.xml"));
	}
}
