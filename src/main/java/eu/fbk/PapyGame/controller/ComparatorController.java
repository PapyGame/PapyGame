package eu.fbk.PapyGame.controller;

// import java.io.BufferedWriter;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.io.StringWriter;
import java.io.File;
// import java.net.URI;
// import java.net.URISyntaxException;

/*
 * Il servizio è stato realizzato dall'Università dell'Aquila e adattato per PapyGame
 * Il progetto originale è disponibile al seguente link: https://github.com/jdirocco/ModelComparator
 */

import java.util.ArrayList;
import java.util.List;

// import javax.xml.transform.*;
// import javax.xml.transform.stream.*;

// import org.eclipse.emf.common.util.BasicMonitor;
// import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
// import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
// import org.eclipse.emf.compare.merge.BatchMerger;
// import org.eclipse.emf.compare.merge.IBatchMerger;
// import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
// import org.eclipse.emf.ecore.EObject;
// import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
// import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
// import org.eclipse.emf.ecore.util.ExtendedMetaData;
// import org.eclipse.emf.ecore.xmi.XMLResource;
// import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;

import eu.fbk.PapyGame.service.discoverer.AdvancedDiscovererService;
// import eu.fbk.PapyGame.service.discoverer.SimpleDiscovererService;

@RestController
@RequestMapping("/api/v1")
public class ComparatorController {

	// @Autowired
	// private SimpleDiscovererService simpleDiscovererController;
	@Autowired
	private AdvancedDiscovererService advancedDiscovererController;

    // private Resource registerMetamodel(String ecoreMetamodel) {
    //     Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());

	// 	ResourceSet rs = new ResourceSetImpl();
	// 	// enable extended metadata
	// 	final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
	// 	rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);

	// 	Resource r = rs.getResource(URI.createFileURI(ecoreMetamodel), true);
	// 	for (EObject eObject : r.getContents()) {
	// 		if (eObject instanceof EPackage) {
	// 			EPackage p = (EPackage) eObject;
	// 			registerSubPackage(p);
	// 		}
	// 	}
	// 	return r;
    // }

    // private void registerSubPackage(EPackage p) {
    //     EPackage.Registry.INSTANCE.put(p.getNsURI(), p);
	// 	for (EPackage pack : p.getESubpackages()) {
	// 		registerSubPackage(pack);
	// 	}
    // }

    // public double calculateSimilarity2(String metamodel, String art1, String art2) {
	@GetMapping("/calculateSimilarity")
	public double calculateSimilarity(@RequestParam String studentJson, @RequestParam String teacherJson) {
        // registerMetamodel(metamodel);

		try {
// Bisogna modificarlo e mettere un personalizzatore al file temporaneo altrimenti si creerebbero problemi
// se due utenti chiamassero l'API contemporaneamente
			File studentECORE = File.createTempFile("student", ".ecore");
			File teacherECORE = File.createTempFile("teacher", ".ecore");

			// simpleDiscovererController.simpleDiscoverer(studentJson, studentECORE);
			// simpleDiscovererController.simpleDiscoverer(teacherJson, teacherECORE);
			advancedDiscovererController.advancedDiscoverer(studentJson, studentECORE);
			advancedDiscovererController.advancedDiscoverer(teacherJson, teacherECORE);

			System.out.println(studentECORE.getAbsolutePath());
			System.out.println(teacherECORE.getAbsolutePath());

			URI studentURI = URI.createFileURI(studentECORE.getAbsolutePath());
			URI teacherURI = URI.createFileURI(teacherECORE.getAbsolutePath());

			// URI studentURI = URI.createURI("./src/main/resources/prova.xml");
			// URI teacherURI = URI.createURI("./src/main/resources/prova2.xml");

			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
			ResourceSet resourceSet1 = new ResourceSetImpl();
			ResourceSet resourceSet2 = new ResourceSetImpl();
			resourceSet1.getResource(studentURI, true);
			resourceSet2.getResource(teacherURI, true);
			
			IComparisonScope scope = new DefaultComparisonScope(resourceSet1, resourceSet2, null);
			// int total = 0;
		
			Comparison comparisonDef = EMFCompare.builder().build().compare(scope);
			List<Match> matchesDef = comparisonDef.getMatches();
			// StringWriter writer = new StringWriter();
			// submatchesPrintTree(matchesDef, 0, writer);
			// String treeString = writer.toString();
			// try (FileWriter fileWriter = new FileWriter("./src/main/resources/matches_tree.txt")) {
			// 	fileWriter.write(treeString);
			//   } catch (IOException e) {
			// 	e.printStackTrace();
			// }
			// int counterDef = 0;
			List<Integer> counterDef_total = new ArrayList<Integer>();
			counterDef_total.add(0);
			counterDef_total.add(0);
			counterDef_total = submatches(matchesDef, counterDef_total);

			// for (Match match : matchesDef) {
			// 	List<Match> lm = new ArrayList<Match>(); 
			// 	match.getAllSubmatches().forEach(lm::add);		
			// 	total += lm.size();
			// 	for (Match match2 : lm) {
			// 		if (match2.getLeft() != null && match2.getRight() != null)
			// 			counterDef++;
			// 	}
			// 	if (match.getLeft() != null && match.getRight() != null)
			// 		counterDef++;
			// }


			System.out.println("Counter: " + counterDef_total.get(0) + " Total: " + counterDef_total.get(1));
			double value = ((counterDef_total.get(0) * 1.0) / counterDef_total.get(1));

			studentECORE.delete();
			teacherECORE.delete();

			return value;
			// return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private List<Integer> submatches(List<Match> matchesDef, List<Integer> counterDef_total) {
		for (Match match : matchesDef) {
			List<Match> lm = new ArrayList<Match>();
			match.getAllSubmatches().forEach(lm::add);
			counterDef_total.set(1, counterDef_total.get(1) + lm.size() + 1);

			for (Match match2 : lm) {
				if (match2.getAllSubmatches() != null) {
					List<Match> lm2 = new ArrayList<Match>();
					match2.getAllSubmatches().forEach(lm2::add);
					counterDef_total = submatches(lm2, counterDef_total);
					counterDef_total.set(0, counterDef_total.get(0) + 1);
				}
			}
			if (match.getLeft() != null && match.getRight() != null)
				counterDef_total.set(0, counterDef_total.get(0) + 1);
		}

		return counterDef_total;
	}




	// private void submatchesPrintTree(List<Match> matchesDef, int level) {
	// 	// Base case: Empty list or no submatches
	// 	if (matchesDef == null || matchesDef.isEmpty()) {
	// 	  return;
	// 	}
	  
	// 	// Print indentation based on level
	// 	for (int i = 0; i < level; i++) {
	// 	  System.out.print("  "); 
	// 	}
	  
	// 	// Process top-level matches
	// 	for (Match match : matchesDef) {
	// 	  // Print information about the match (e.g., type, left, right)
	// 	  System.out.println(match.toString()); 
	  
	// 	  // Recursively print submatches
	// 	  List<Match> submatches = new ArrayList<Match>();
	// 	  match.getAllSubmatches().forEach(submatches::add);
	// 	  if (submatches != null) {
	// 		submatchesPrintTree(submatches, level + 1);
	// 	  }
	// 	}
	//   }





	//   private void submatchesPrintTree(List<Match> matchesDef, int level, StringWriter writer) {
	// 	// Base case: Empty list or no submatches
	// 	if (matchesDef == null || matchesDef.isEmpty()) {
	// 	  return;
	// 	}
	  
	// 	// Print indentation based on level
	// 	for (int i = 0; i < level; i++) {
	// 	  writer.append("  "); 
	// 	}
	  
	// 	// Process top-level matches
	// 	for (Match match : matchesDef) {
	// 	  // Print information about the match (e.g., type, left, right)
	// 	  writer.append(match.toString()).append("\n"); 
	  
	// 	  // Recursively print submatches
	// 	  List<Match> submatches = new ArrayList<Match>();
	// 	  match.getAllSubmatches().forEach(submatches::add);
	// 	  if (submatches != null) {
	// 		submatchesPrintTree(submatches, level + 1, writer);
	// 	  }
	// 	}
	//   }






	// @GetMapping("/calculateSimilarity2")
	// double calculateSimilarity2(@RequestParam String studentJson, @RequestParam String teacherJson) {
		
	// 	Object jsonData1, jsonData2;
	// 	try {
	// 		ObjectMapper mapper = new ObjectMapper();
	// 		jsonData1 = mapper.readValue(studentJson, Object.class);
	// 		jsonData2 = mapper.readValue(teacherJson, Object.class);
	// 	} catch (Exception e) {
	// 		return 0;
	// 	}
		
	// 	List<Object> tree1 = convertDataToTree(jsonData1);
	// 	List<Object> tree2 = convertDataToTree(jsonData2);

	// 	double similarityScore = 0;
	// 	similarityScore += compareNodes(tree1, tree2, 1);

	// 	similarityScore /= (tree1.size() + tree2.size());
	// 	return similarityScore;
	// }






	// // Recursive function to compare tree nodes and accumulate similarity scores
	// double compareNodes(List<Object> tree1, List<Object> tree2, double weight) {
	// 	// Check if trees are empty or have different sizes
	// 	if (tree1.isEmpty() || tree2.isEmpty() || tree1.size() != tree2.size()) {
	// 		return 0;
	// 	}

	// 	// Compare node types (e.g., primitive values, lists, maps)
	// 	if (!tree1.get(0).getClass().equals(tree2.get(0).getClass())) {
	// 		return 0;
	// 	}

	// 	if (tree1.get(0) instanceof Number || tree1.get(0) instanceof String) {
			
	// 		return weight;
	// 	}

	// 	System.out.println(tree1.get(0).getClass());
	// 	System.out.println(tree2.get(0).getClass());

	// 	// Recursively compare nested lists or maps
	// 	double childSimilarity = 0;
	// 	for (int i = 0; i < tree1.size(); i++) {
	// 		childSimilarity += compareNodes((List<Object>) tree1.get(i), (List<Object>) tree2.get(i), weight / 2);
	// 	}

	// 	return childSimilarity;
	// }





	public List<Object> convertDataToTree(Object data) {
        if (data instanceof List) {
            List<Object> tree = new ArrayList<>();
            for (Object item : (List<?>) data) {
                tree.add(convertDataToTree(item));
            }
            return tree;
		} else if (data instanceof Map) {
			List<Object> tree = new ArrayList<>();
			for (Map.Entry<?, ?> entry : ((Map<?, ?>) data).entrySet()) {
				List<Object> child = new ArrayList<>();
				child.add(entry.getKey());
				child.add(convertDataToTree(entry.getValue()));
				tree.add(child);
			}
			return tree;
		} else {
			return List.of(data);
		}
    }
}
