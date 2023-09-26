package helpers;

import com.microsoft.playwright.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Java class (PageHelper) manages browser pages, browser setup, and provides methods to read JSON objects for page
    objects and routes. It encapsulates browser-related logic and configuration for web automation using Playwright.
*/

public class PageHelper {

    private final Map<String, Page> pages = new HashMap<>();
    private final Browser browser;
    private final BrowserContext browserContext;


    public PageHelper() {
        try{
            Playwright playwright = Playwright.create();

            boolean isHeadless = Boolean.parseBoolean(PropertyHelper.getProperty("headlessOption"));
            String browserType = PropertyHelper.getProperty("browser");

            String width = PropertyHelper.getProperty("browserWidth");
            String height = PropertyHelper.getProperty("browserHeight");

            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(isHeadless);
            if (width.equals("") || height.equals("")) {
                launchOptions.setArgs(List.of("--start-maximized"));
            }

            if ("chromium".equalsIgnoreCase(browserType)) {
                browser = playwright.chromium().launch(launchOptions);
            } else if ("webkit".equalsIgnoreCase(browserType)) {
                browser = playwright.webkit().launch(launchOptions);
            } else if ("firefox".equalsIgnoreCase(browserType)) {
                browser = playwright.firefox().launch(launchOptions);
            } else if ("edge".equalsIgnoreCase(browserType)) {
                launchOptions.setChannel("msedge");
                browser = playwright.chromium().launch(launchOptions);
            } else if ("chrome".equalsIgnoreCase(browserType)) {
                launchOptions.setChannel("chrome");
                browser = playwright.chromium().launch(launchOptions);
            } else {
                throw new IllegalArgumentException("Invalid browser type: " + browserType);
            }

            if (width.equals("") || height.equals("")) {
                browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
            } else {
                browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(Integer.parseInt(width), Integer.parseInt(height)));
            }

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }


    public synchronized Page getPage(String id){
        if(!pages.containsKey(id)){
            Page page = browserContext.newPage();
            pages.put(id, page);

        } return  pages.get(id);
    }


    public synchronized void close() {
        for (Page page : pages.values()) {
            page.close();
        }
        pages.clear();
        browser.close();
    }


    public static String readPageObjects(String fileName, String key){

        JsonHelper jsonReader = new JsonHelper();
        jsonReader.readJSONFiles(PropertyHelper.getProperty("pagePath"));

        //        System.out.println("Value for key " + key + " in file " + fileName + ".json: " + value);
        return jsonReader.getValue(fileName, key);
    }

    public static String readRoutesObjects(String key){

        JsonHelper jsonReader = new JsonHelper();
        jsonReader.readJSONFiles(PropertyHelper.getProperty("routePath"));

        //        System.out.println("Value for key " + key + " in routes.json: " + value);
        return jsonReader.getValue("routes", key);
    }
}
