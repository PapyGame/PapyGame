package eu.fbk.PapyGame.service.jdd;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

/* 
 * jdd.js - JSON Diff Checker
 * Located in /src/main/resources/static/js/jdd.js
 */

@Service
public class JsonDiffCheckerService {
    public void jsonDiffChecker(String json1, String json2) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        
        Map<String, Object> map1 = null;
        Map<String, Object> map2 = null;
        try {
            map1 = mapper.readValue(json1, typeRef);
            map2 = mapper.readValue(json2, typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MapDifference<String, Object> difference = Maps.difference(map1, map2);

        System.out.println("Entries only on the left\n--------------------------");
        difference.entriesOnlyOnLeft()
                .forEach((key, value) -> System.out.println(key + ": " + value));

        System.out.println("\n\nEntries only on the right\n--------------------------");
        difference.entriesOnlyOnRight()
                .forEach((key, value) -> System.out.println(key + ": " + value));

        System.out.println("\n\nEntries differing\n--------------------------");
        difference.entriesDiffering()
                .forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
