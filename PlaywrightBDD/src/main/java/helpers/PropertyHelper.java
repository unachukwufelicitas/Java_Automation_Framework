package helpers;

import  java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/*
     Java class (PropertyHelper) provides utility methods to read property values from configuration files.
     It encapsulates the logic for reading properties, making it easier to access and use configuration data in the
     application or test scripts.
 */

public class PropertyHelper {
    public static String getProperty(String key){

        Properties properties = new Properties();
        String value = null;

        try{
            FileInputStream fileInputStream = new FileInputStream("src/test/config/config.properties");
            properties.load(fileInputStream);

            value = properties.getProperty(key);
        }catch (IOException exception){
            exception.printStackTrace();
        }
        return value;
    }

    public static String getAuth(String key){

        Properties properties = new Properties();
        String value = null;

        try{
            FileInputStream fileInputStream = new FileInputStream("src/test/config/auth.properties");
            properties.load(fileInputStream);

            value = properties.getProperty(key);
        }catch (IOException exception){
            exception.printStackTrace();
        }
        return value;
    }
}
