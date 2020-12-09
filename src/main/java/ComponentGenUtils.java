import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentGenUtils {
    public static String toPascalCase(String input) {
        List<String> words = new ArrayList<>();
        Matcher m = Pattern.compile("[A-Z\\xC0-\\xD6\\xD8-\\xDE]?[a-z\\xDF-\\xF6\\xF8-\\xFF]+|[A-Z\\xC0-\\xD6\\xD8-\\xDE]+(?![a-z\\xDF-\\xF6\\xF8-\\xFF])|\\d+")
                .matcher(input);
        while(m.find()) {
            words.add(m.group());
        }

        String inputCamelCase = "";
        for(int i = 0; i < words.size(); i++) {
            String tmp = words.get(i);
            tmp = tmp.toLowerCase();
            tmp = tmp.substring(0, 1).toUpperCase() + tmp.substring(1);

            inputCamelCase += tmp;
        }
        return inputCamelCase.replaceAll(" ", "");
    }

    // KV pair = Variable: value replacing with
    public static String insertIntoTemplate(HashMap<String,String> kvPair, String template) {

        Matcher m = Pattern.compile("\\$\\{(.+?)\\}", Pattern.MULTILINE).matcher(template);
        StringBuilder sb = new StringBuilder();

        while(m.find()) {
            String regexMatch = m.group().substring(2, m.group().length()-1);
            if(kvPair.containsKey(regexMatch)) {
                m.appendReplacement(sb, kvPair.get(regexMatch));
            }
        }
        m.appendTail(sb);

        return sb.toString();
    }
}