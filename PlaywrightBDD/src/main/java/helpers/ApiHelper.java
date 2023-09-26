package helpers;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import java.util.HashMap;
import java.util.Map;

/*
    Java class (ApiHelper) is responsible for making API requests using Playwright, caching and managing API responses,
    and providing a way to clean up resources when the APIHelper is no longer in use. It encapsulates API request logic
    and makes it easier to work with APIs in a test automation context.
*/

public class ApiHelper {

    private final Playwright playwright;
    private final APIRequest request;

    private APIResponse response;
    private static APIRequestContext requestContext = null;
    private static final Map<String, APIResponse> responses = new HashMap<>();

    public ApiHelper() {
        try {
            playwright = Playwright.create();
            request = playwright.request();
            requestContext = request.newContext();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public synchronized APIResponse getResponse(String id, String url, RequestOptions options){
        String route = PropertyHelper.getProperty("apiBaseUrl") + url;
        if(!responses.containsKey(id) && options == null){
            response = requestContext.get(route);
            responses.put(id, response);

        } else{
            response = requestContext.get(route, options);
            responses.put(id, response);
        }

        return  responses.get(id);
    }

    public synchronized APIResponse sendRequest(String id, String url, RequestOptions options){
        String route = PropertyHelper.getProperty("apiBaseUrl") + url;
        if(!responses.containsKey(id)){
            response = requestContext.post(route, options);
            responses.put(id, response);

        } return  responses.get(id);
    }

    // update selected field
    public synchronized APIResponse patchRequest(String id, String url, String identifier, RequestOptions options){
        String route = PropertyHelper.getProperty("apiBaseUrl") + url+"/"+identifier;
        if(!responses.containsKey(id) && options == null){
            response = requestContext.patch(route);
            responses.put(id, response);

        } else{
            response = requestContext.patch(route, options);
            responses.put(id, response);
        }

        return  responses.get(id);
    }

    // updates all the fields
    public synchronized APIResponse putRequest(String id, String url, String identifier, RequestOptions options){
        String route = PropertyHelper.getProperty("apiBaseUrl") + url+"/"+identifier;
        if(!responses.containsKey(id) && options == null){
            response = requestContext.put(route);
            responses.put(id, response);

        } else{
            response = requestContext.put(route, options);
            responses.put(id, response);
        }

        return  responses.get(id);
    }

    public synchronized APIResponse deleteRequest(String id, String url, String identifier, RequestOptions options){
        String route = PropertyHelper.getProperty("apiBaseUrl") + url+"/"+identifier;
        if(!responses.containsKey(id) && options == null){
            response = requestContext.delete(route);
            responses.put(id, response);

        } else{
            response = requestContext.delete(route, options);
            responses.put(id, response);
        }

        return  responses.get(id);
    }



    public synchronized void close() {
        for (APIResponse response : responses.values()) {
            response.dispose();
        }
        requestContext.dispose();
        playwright.close();

    }


}
