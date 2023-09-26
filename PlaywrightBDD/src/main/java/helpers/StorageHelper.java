package helpers;

import java.util.HashMap;

/*
     Java class (StorageHelper) provides a basic key-value storage mechanism using a HashMap.
     It allows you to store and retrieve values by their keys, making it useful for maintaining state or
     sharing data between different parts of an application or test suite.
 */

public class StorageHelper {
    private static final HashMap<String, String> keyValuePairs = new HashMap<>();

    public static void store(String key, String value) {
        keyValuePairs.put(key, value);
    }

    public static String getValue(String key) {
        return keyValuePairs.get(key);
    }
}
