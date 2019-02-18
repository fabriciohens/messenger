package context;

import java.util.HashMap;
import java.util.Map;

public class IntegrationTestContext {

    private static Map<String, Object> context;

    static {
        context = new HashMap<>();
    }

    public static Object put(String key, Object o) {
        return context.put(key, o);
    }

    public static Object get(String key) {
        return context.get(key);
    }
}
