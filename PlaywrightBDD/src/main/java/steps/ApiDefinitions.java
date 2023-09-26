package steps;


import com.deque.html.axecore.playwright.AxeBuilder;
import com.deque.html.axecore.results.AxeResults;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import helpers.PropertyHelper;
import helpers.StorageHelper;
import hooks.Hooks;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import org.testng.Assert;
import org.testng.asserts.Assertion;

import java.io.IOException;
import java.util.*;

import static org.testng.Assert.assertEquals;

public class ApiDefinitions {

    private APIResponse response;

    @When("^I make a GET request from the \"(.*)\" route$")
    public void GetFunction(String route) throws IOException {
        response = Hooks.getApiManager().getResponse((Thread.currentThread().getName()), route, null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResponse =  objectMapper.readTree(response.body());
        System.out.println(postJsonResponse.toPrettyString());
    }

    @When("^I make a GET request from the \"(.*)\" route, where data contains:$")
    public void GetFunction(String route, DataTable dataTable) throws IOException {

        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : data) {
            String key = row.get("key");
            String value = row.get("value");


            RequestOptions auth = RequestOptions.create()
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Authorization", PropertyHelper.getProperty("apiToken"))
                    .setQueryParam(key, value);

            response = Hooks.getApiManager().getResponse((Thread.currentThread().getName()), route, auth);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode postJsonResponse =  objectMapper.readTree(response.body());
            System.out.println(postJsonResponse.toPrettyString());
        }

    }


    @When("^I make a single GET request from the \"(.*)\" route and store \"(.*)\" as \"(.*)\", where data contains:$")
    public void GetFunction(String route, String pk, String storageKey, DataTable dataTable) throws IOException {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : data) {
            String key = row.get("key");
            String value = row.get("value");

            RequestOptions auth = RequestOptions.create()
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Authorization", PropertyHelper.getProperty("apiToken"))
                    .setQueryParam(key, value);

            response = Hooks.getApiManager().getResponse((Thread.currentThread().getName()), route, auth);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResponse = objectMapper.readTree(response.body());
        System.out.println(postJsonResponse.toPrettyString());

        if (pk != null) {
            if (postJsonResponse.isArray()) {
                // Iterate over the array elements and check if 'pk' exists in any of them
                for (JsonNode node : postJsonResponse) {
                    JsonNode pkNode = node.get(pk);
                    if (pkNode != null) {
                        String userId = pkNode.asText();
                        System.out.println("The " + route + " contains " + userId);
                        StorageHelper.store(storageKey, userId);
                        break; // Exit the loop after finding the first occurrence
                    }
                }
            } else {
                // Handle the situation when the response is not an array
                JsonNode pkNode = postJsonResponse.get(pk);
                if (pkNode != null) {
                    String userId = pkNode.asText();
                    System.out.println("The " + route + " contains " + userId);
                    StorageHelper.store(storageKey, userId);
                } else {
                    System.out.println("Key " + pk + " not found in the JSON response.");
                    // Handle the situation when the key is not found
                }
            }
        } else {
            System.out.println("pk is null.");
            // Handle the situation when pk is null
        }

        Assert.assertEquals(response.status(), 200);

    }


    @When("^on the \"(.*)\" route, I POST the following data:$")
    public void createUserWithDataTable(String route, DataTable dataTable) throws IOException {

        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        Map<String, String> requestData = new HashMap<>();
        for (Map<String, String> row : data) {
            String key = row.get("key");
            String value = row.get("value");

            requestData.put(key, value);
        }

        RequestOptions requestOptions = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", PropertyHelper.getProperty("apiToken"))
                .setData(requestData);


        response = Hooks.getApiManager().sendRequest((Thread.currentThread().getName()), route, requestOptions);


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResponse =  objectMapper.readTree(response.body());
        System.out.println(postJsonResponse.toPrettyString());

    }

    // update without creating a new entry
    @When("^on the \"(.*)\" route with \"(.*)\", I PUT the following data:$")
    public void editUserWithDataTable(String route, String identifier, DataTable dataTable) throws IOException {

        String localId =  StorageHelper.getValue(identifier);
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        Map<String, String> requestData = new HashMap<>();
        for (Map<String, String> row : data) {
            String key = row.get("key");
            String value = row.get("value");

            requestData.put(key, value);
        }

        RequestOptions requestOptions = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", PropertyHelper.getProperty("apiToken"))
                .setData(requestData);


        response = Hooks.getApiManager().putRequest((Thread.currentThread().getName()), route, localId, requestOptions);

        System.out.println(response.status());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResponse =  objectMapper.readTree(response.body());
        System.out.println(postJsonResponse.toPrettyString());
        Assert.assertTrue(response.ok());

    }

    @When("^on the \"(.*)\" route with \"(.*)\", I PATCH the following data:$")
    public void editUserWithPatchDataTable(String route, String identifier, DataTable dataTable) throws IOException {

        String localId =  StorageHelper.getValue(identifier);
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        Map<String, String> requestData = new HashMap<>();
        for (Map<String, String> row : data) {
            String key = row.get("key");
            String value = row.get("value");

            requestData.put(key, value);
        }

        RequestOptions requestOptions = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", PropertyHelper.getProperty("apiToken"))
                .setData(requestData);


        response = Hooks.getApiManager().patchRequest((Thread.currentThread().getName()), route, localId, requestOptions);

        System.out.println(response.status());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResponse =  objectMapper.readTree(response.body());
        System.out.println(postJsonResponse.toPrettyString());
        Assert.assertTrue(response.ok());

    }

    @When("^I perform delete operation, on the \"(.*)\" route with \"(.*)\"$")
    public void deleteUserWithPatchDataTable(String route, String identifier) throws IOException {

        String localId =  StorageHelper.getValue(identifier);


        RequestOptions requestOptions = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", PropertyHelper.getProperty("apiToken"));

        response = Hooks.getApiManager().deleteRequest((Thread.currentThread().getName()), route, localId, requestOptions);

        System.out.println(response.status());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResponse =  objectMapper.readTree(response.body());
        System.out.println(postJsonResponse.toPrettyString());
//        Assert.assertEquals(response.status(), 404);

    }
}
