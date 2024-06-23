package eu.fbk.PapyGame.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Service
public class JsonMapperService {

    String filePath = "./src/main/resources/universita.txt";
    static FileWriter writer;

    static int countModels = 0;
    static int countClasses = 0;
    static int countProperties = 0;
    static int countOperations = 0;
    static int countParameters = 0;
    static int countAssociations = 0;
    static int countPrimitiveTypes = 0;

    public void jsonMapper(String json) {    
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());

        try {
            writer = new FileWriter(filePath);
            writer.write("");
            writer = new FileWriter(filePath, true);
            printMap(map);
            writer.write("Models: " + countModels + "\n");
            writer.write("Classes: " + countClasses + "\n");
            writer.write("Properties: " + countProperties + "\n");
            writer.write("Operations: " + countOperations + "\n");
            writer.write("Parameters: " + countParameters + "\n");
            writer.write("Associations: " + countAssociations + "\n");
            writer.write("PrimitiveTypes: " + countPrimitiveTypes + "\n");
            writer.close();
            System.out.println("Models: " + countModels);
            System.out.println("Classes: " + countClasses);
            System.out.println("Properties: " + countProperties);
            System.out.println("Operations: " + countOperations);
            System.out.println("Parameters: " + countParameters);
            System.out.println("Associations: " + countAssociations);
            System.out.println("PrimitiveTypes: " + countPrimitiveTypes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printMap(Map<String, Object> map) throws IOException{
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": ");
            writer.write(entry.getKey() + ": \n");
            printValue(entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    private static void printValue(Object value) throws IOException{
        if (value instanceof List) {
            printList((List<?>) value);
        } else if (value instanceof Map) {
            printMap((Map<String, Object>) value);
        } else {
            writer.write("\t" + value + "\n");
            System.out.println("\t" + value);
            counter(value.toString());
        }
    }

    private static void printList(List<?> list) throws IOException{
        for (Object obj : list) {
            printValue(obj);
        }
    }

    private static void counter(String key) {
        switch (key) {
            case "uml:Model":
                countModels++;
                break;
            case "uml:Class":
                countClasses++;
                break;
            case "uml:Property":
                countProperties++;
                break;
            case "uml:Operation":
                countOperations++;
                break;
            case "uml:Parameter":
                countParameters++;
                break;
            case "uml:Association":
                countAssociations++;
                break;
            case "uml:PrimitiveType":
                countPrimitiveTypes++;
                break;
        }
    }
}
