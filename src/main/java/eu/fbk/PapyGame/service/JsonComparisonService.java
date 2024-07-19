package eu.fbk.PapyGame.service;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JsonComparisonService {

    public List<String> compareJson(JsonNode teacher, JsonNode student) throws Exception {
        // JsonNode tree1 = objectMapper.readTree(teacher);
        // JsonNode tree2 = objectMapper.readTree(student);

        List<String> differences = new ArrayList<>();
        compareJsonNodes("", teacher, student, differences);
        return differences;
    }

    private void compareJsonNodes(String path, JsonNode node1, JsonNode node2, List<String> differences) {
        if (node1 == null && node2 != null) {
            differences.add(path + " added: " + node2);
        } else if (node1 != null && node2 == null) {
            differences.add(path + " removed: " + node1);
        } else if (node1.isObject() && node2.isObject()) {
            Set<String> fieldNames = new HashSet<>();
            node1.fieldNames().forEachRemaining(fieldNames::add);
            node2.fieldNames().forEachRemaining(fieldNames::add);

            for (String fieldName : fieldNames) {
                compareJsonNodes(path + "/" + fieldName, node1.get(fieldName), node2.get(fieldName), differences);
            }
        } else if (node1.isArray() && node2.isArray()) {
            if (isSomething(path, "ownedAttribute") || isSomething(path, "ownedOperation")) {
                compareArray(path, node1, node2, differences);
            } else {
                int size = Math.max(node1.size(), node2.size());
                for (int i = 0; i < size; i++) {
                    compareJsonNodes(path + "[" + i + "]", node1.get(i), node2.get(i), differences);
                }
            }
        } else if (!isSomething(path, "id") && !node1.equals(node2)) {
            differences.add(path + " changed from " + node1 + " to " + node2);
        }
    }

    private void compareArray(String path, JsonNode node1, JsonNode node2, List<String> differences) {
        List<JsonNode> attributes1 = getAttributesList(node1);
        List<JsonNode> attributes2 = getAttributesList(node2);

        for (JsonNode attr1 : attributes1) {
            boolean found = false;
            for (JsonNode attr2 : attributes2) {
                if (areAttributesEqual(attr1, attr2)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                differences.add(path + " removed: " + attr1);
            }
        }

        for (JsonNode attr2 : attributes2) {
            boolean found = false;
            for (JsonNode attr1 : attributes1) {
                if (areAttributesEqual(attr1, attr2)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                differences.add(path + " added: " + attr2);
            }
        }
    }

    private List<JsonNode> getAttributesList(JsonNode node) {
        List<JsonNode> list = new ArrayList<>();
        if (node != null && node.isArray()) {
            for (JsonNode element : node) {
                list.add(element);
            }
        }
        return list;
    }

    private boolean areAttributesEqual(JsonNode node1, JsonNode node2) {
        if (node1.has("data") && node2.has("data")) {
            JsonNode data1 = node1.get("data");
            JsonNode data2 = node2.get("data");
            if (data1.has("name") && data2.has("name")) {
                return data1.get("name").asText().equals(data2.get("name").asText());
            }
        }
        return false;
    }

    private boolean isSomething(String path, String something) {
        return path.endsWith("/" + something);
    }

    public Map<String, Integer> getConstraints(JsonNode json) throws Exception {
        Map<String, Integer> counter = new HashMap<>();
        counter.put("classes", 0);
        counter.put("attributes", 0);
        counter.put("operations", 0);
        counter.put("associations", 0);
        
        JsonNode packagedElements = json.path("content").get(0).path("data").path("packagedElement");
        Iterator<JsonNode> elements = packagedElements.elements();
        
        while (elements.hasNext()) {
            JsonNode element = elements.next();
            String eClass = element.path("eClass").asText();
            
            // Conta le classi
            if (eClass.equals("uml:Class")) {
                counter.put("classes", counter.get("classes") + 1);
                
                // Conta gli attributi della classe
                JsonNode attributes = element.path("data").path("ownedAttribute");
                counter.put("attributes", counter.get("attributes") + attributes.size());

                // Conta i metodi della classe
                JsonNode operations = element.path("data").path("ownedOperation");
                counter.put("operations", counter.get("operations") + operations.size());
            }

            // Conta le relazioni
            if (eClass.equals("uml:Association")) {
                counter.put("associations", counter.get("associations") + 1);
            }
        }

        counter.put("associations", counter.get("associations"));

        return counter;
    }
}