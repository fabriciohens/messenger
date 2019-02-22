package context;

import java.util.HashMap;
import java.util.Map;

public class IntegrationTestContext {

    private static Map<String, Object> context;

    static {
        context = new HashMap<>();
    }

    public static void putNewObject(String key, Object o) {
        if (context.containsKey(key)) {
            throw new IllegalArgumentException("There is already a object in the context with key: " + key);
        }
        context.put(key, o);
    }

    public static Object updateObject(String key, Object o) {
        return context.put(key, o);
    }

    public static Object getObject(String key) {
        return context.get(key);
    }
}
