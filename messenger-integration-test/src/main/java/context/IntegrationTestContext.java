package context;

import java.util.HashMap;
import java.util.Map;

public class IntegrationTestContext {

    private static Map<String, Object> context;

    static {
        context = new HashMap<>();
    }

    public static void putNewObject(final String key, final Object object) {
        String formedKey = formKey(key);
        if (context.containsKey(formedKey)) {
            throw new IllegalArgumentException("There is already a object in the context with key: " + key);
        }
        context.put(formedKey, object);
    }

    public static void updateObject(final String key, final Object o) {
        context.put(formKey(key), o);
    }

    public static Object getObject(final String key) {
        return context.get(formKey(key));
    }

    private static String formKey(final String key) {
        long id = Thread.currentThread().getId();
        return String.valueOf(id).concat(key);
    }
}
