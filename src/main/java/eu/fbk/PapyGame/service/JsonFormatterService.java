package eu.fbk.PapyGame.service;

import org.springframework.stereotype.Service;

@Service
public class JsonFormatterService {
    
    private String repeat (String s, int count) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public String format(String json) {
        int     i           = 0;
        int     il          = 0;
        String  tab         = "    ";
        String  newJson     = "";
        int     indentLevel = 0;
        boolean inString    = false;
        char    currentChar;

        for (i = 0, il = json.length(); i < il; i++) {
            currentChar = json.charAt(i);

            switch (currentChar) {
                case '{':
                case '[':
                    if (!inString) {
                        newJson += currentChar + "\n" + repeat(tab, ++indentLevel);
                    } else {
                        newJson += currentChar;
                    }
                    break;
                case '}':
                case ']':
                    if (!inString) {
                        newJson += "\n" + repeat(tab, --indentLevel) + currentChar;
                    } else {
                        newJson += currentChar;
                    }
                    break;
                case ',':
                    if (!inString) {
                        newJson += ",\n" + repeat(tab, indentLevel);
                    } else {
                        newJson += currentChar;
                    }
                    break;
                case ':':
                    if (!inString) {
                        newJson += ": ";
                    } else {
                        newJson += currentChar;
                    }
                    break;
                case '\n':
                case '\r':
                case '\t':
                    break;
                case '"':
                    inString = !inString;
                default:
                    newJson += currentChar;
                    break;
            }
        }

        return newJson;
    }
}
