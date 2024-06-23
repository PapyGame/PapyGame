// package eu.fbk.PapyGame.controller;

// import java.nio.file.Paths;

// import org.json.JSONObject;
// import org.json.XML;

// import org.springframework.web.bind.annotation.RestController;

// import java.nio.file.Files;

// @RestController
// public class JsonToXMLController {

//     public static String generateXMLFromJson(String fileJson) {
//         try {
//             String stringa = new String(Files.readAllBytes(Paths.get(fileJson)));
//             // JSONObject jsonObject = new JSONObject(fileJson);
//             JSONObject jsonObject = new JSONObject(stringa);
//             String xmlString = XML.toString(jsonObject);
            
//             return xmlString;
//         } catch (Exception e) {
//             e.printStackTrace();
//             return null;
//         }
//     }
// }
