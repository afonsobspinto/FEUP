import java.util.HashMap;

public class Utils {
    private static HashMap<String,String> conditionalsHashMap= new HashMap<String,String>();
    private static HashMap<String,String> operationsHashMap= new HashMap<String,String>();

    static {
        conditionalsHashMap.put(">", "if_icmpgt");
        conditionalsHashMap.put(">=", "if_icmpge");
        conditionalsHashMap.put("<", "if_icmplt");
        conditionalsHashMap.put("<=", "if_icmple");
        conditionalsHashMap.put("==", "if_icmpeq");
        conditionalsHashMap.put("!=", "if_icmpne");
        
        operationsHashMap.put("+", "iadd");
        operationsHashMap.put("-", "isub");
        operationsHashMap.put("*", "imul");
        operationsHashMap.put("/", "idiv");
    }
}
