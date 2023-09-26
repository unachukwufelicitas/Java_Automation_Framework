package helpers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class JsonHelper {

    private final HashMap<String, JSONObject> jsonObjects = new HashMap<>();

    /*
    readJSONFiles(String directory): This method takes a directory path as input, and
    recursively reads all the JSON files present in the directory and its subdirectories.
    For each file, it creates a JSONObject from the contents of the file and stores it in a
    HashMap with the key as the filename (without the .json extension) and the value as the
    JSONObject instance.
     */
    public void readJSONFiles(String directory) {
        /*
        This line of code creates a new File object dir using the directory parameter as the file path.

        The directory parameter is a String that represents the file path of a directory to read JSON files from.
        The File class in Java represents a file or directory path name.

        The File constructor takes a String parameter that represents the file path. In this case, the directory
        parameter is passed as the file path to create a new File object that represents the directory.
         */
        File dir = new File(directory);
        if (dir.exists() && dir.isDirectory()) {
            /*
            listFiles() - method is used to retrieve an array of File objects that represent the files
            and directories in the specified directory.
             */
            File[] files = dir.listFiles();

            assert files != null; //check if null
            for (File file : files) {
                if (file.isDirectory()) {
                    readJSONFiles(file.getAbsolutePath());
                } else {
                    try {
                    /*
                    This line of code creates a new instance of the JSONParser class from the org.json.simple.parser package.

                    The JSONParser class provides methods for parsing JSON data from a variety of input sources, including
                    InputStream, Reader, and String. In this case, the FileReader class is used to read the contents of the
                    file object passed as a parameter to the parse() method.

                    The JSONParser instance is then used to parse the JSON data from the file into a Java object.
                    The parse() method of the JSONParser class returns an Object representing the parsed JSON data,
                    which is then cast to a JSONObject instance.
                     */
                        JSONParser parser = new JSONParser();
                    /*
                    This line of code uses the parse() method of the JSONParser class to parse the contents of the
                    JSON file specified by the file object into a Java object.

                    The parse() method takes an input source as a parameter, which in this case is a FileReader
                    object that reads the contents of the specified JSON file.

                    The parse() method returns an Object that represents the parsed JSON data, which can be cast to
                    various types depending on the structure of the JSON data. In this case, the Object is cast to a
                    JSONObject instance because the input JSON data is assumed to represent a JSON object.
                     */
                        Object obj = parser.parse(new FileReader(file));
                    /*
                    This line of code casts the Object returned by the parse() method of the JSONParser
                    class to a JSONObject instance.

                    The Object returned by the parse() method represents the parsed JSON data, which can be
                    cast to various types depending on the structure of the JSON data. In this case, the Object
                    is cast to a JSONObject instance because the input JSON data is assumed to represent a JSON object.

                    The JSONObject class is part of the org.json.simple package and represents a JSON object, which
                    is an unordered collection of key-value pairs. Each key in the object is a string, and the
                    value can be a JSON object, array, string, number, boolean, or null.
                     */
                        JSONObject jsonObject = (JSONObject) obj;
                    /*
                    This line of code gets the name of the file represented by the file object and assigns it to a new
                    String variable named fileName.

                    The getName() method of the File class returns the name of the file or directory represented by the
                    File object, which in this case is the name of the JSON file being processed.

                    The fileName variable is then used to extract the key for the JSONObject in the jsonObjects map by
                    removing the file extension. The key is the part of the file name before the last period character ('.'),
                    which separates the file name from the file extension.
                     */
                        String fileName = file.getName();
                    /*
                    This line of code extracts the key for the JSONObject in the jsonObjects map from the fileName variable.

                    The substring() method of the String class is used to extract a portion of the fileName string. The method
                    takes two parameters: the starting index of the substring (inclusive) and the ending index of the substring (exclusive).

                    In this case, the starting index is 0, which means the substring starts at the beginning of the fileName string.
                    The ending index is determined by the lastIndexOf() method of the String class, which returns the index of the last
                    occurrence of the specified character or substring in the string.

                    In this case, the lastIndexOf() method is used to find the index of the last period character ('.'), which
                    separates the file name from the file extension. The substring() method is then used to extract the part of
                    the fileName string before the period character, which represents the key for the JSONObject in the jsonObjects map.
                     */
                        String key = fileName.substring(0, fileName.lastIndexOf('.'));
                    /*
                    This line of code stores a JSONObject instance in the jsonObjects map, with the key variable as the key and
                    the jsonObject variable as the value.

                    The jsonObjects map is a HashMap object that is used to store the JSONObject instances parsed from the JSON files.
                    Each JSONObject instance is associated with a key, which is derived from the file name by removing the file extension.

                    The put() method of the HashMap class is used to store a key-value pair in the map. The first parameter to the method
                    is the key to store, and the second parameter is the value to associate with the key.
                     */
                        jsonObjects.put(key, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            // Handle the case where the directory does not exist or is not a directory
            System.out.println("Directory does not exist or is not a directory!");
        }
    }
    /*
    getValue(String fileName, String key): This method takes a filename (without the .json extension)
     and a key as input, and retrieves the value associated with the key in the JSONObject
     corresponding to the given filename.
     */
    public String getValue(String fileName, String key) {
        /*
        This line of code retrieves the JSONObject instance associated with the fileName key from the jsonObjects map.

        The jsonObjects map is a HashMap object that is used to store the JSONObject instances parsed from the JSON files.
        Each JSONObject instance is associated with a key, which is derived from the file name by removing the file extension.

        The get() method of the HashMap class is used to retrieve the value associated with a specific key. The parameter to
        the get() method is the key to retrieve.

        In this case, the fileName variable represents the key to retrieve, and the jsonObject variable is assigned the
        JSONObject instance associated with that key. The getValue() method uses this JSONObject instance to retrieve a
        specific value from the JSON object, based on a second key.
         */
        JSONObject jsonObject = jsonObjects.get(fileName);
        /*
        This line of code retrieves a specific value from the JSONObject instance associated with a specific key,
        and returns it as a String.

        The jsonObject variable represents a JSONObject instance retrieved from the jsonObjects map. The key
        parameter represents the key to use to retrieve the specific value from the JSON object.

        The get() method of the JSONObject class is used to retrieve a specific value from the JSON object based on a key.
        The parameter to the get() method is the key to retrieve.

        In this case, the get() method is called with the key parameter to retrieve the value associated with that key from
        the jsonObject instance. The value is returned as an Object, which is then cast to a String and returned by the getValue() method.

        For example, if the jsonObject instance contains the key-value pair "name": "John", and the key parameter is "name",
        then this line of code will return the String "John".
         */
        return (String) jsonObject.get(key);
    }
}
