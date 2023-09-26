package hooks;

import helpers.ApiHelper;
import helpers.PageHelper;
import helpers.PropertyHelper;
import io.cucumber.java.*;

/*
    Java class contains Cucumber hooks that handle setup and teardown actions for scenarios.
    It initializes helper classes based on the "testType" property, captures screenshots on step failures for
    UI tests, and provides access to these helper classes for use in step definitions.
*/

public class Hooks {

    private static  final ThreadLocal<PageHelper> pageHelper = new ThreadLocal<>();
    private static  final ThreadLocal<ApiHelper> apiHelper = new ThreadLocal<>();
    private static final ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();
    @Before
    public void setUp(Scenario scenario) {
        currentScenario.set(scenario);

        if ("api".equalsIgnoreCase(PropertyHelper.getProperty("testType"))){
            apiHelper.set(new ApiHelper());
        }else if ("ui".equalsIgnoreCase(PropertyHelper.getProperty("testType"))){
            pageHelper.set(new PageHelper());
        }else{
            System.out.println("Please enter either 'ui' or 'api' as testType to proceed!!");
        }
    }

    @AfterStep
    public void afterStep() {
        Scenario scenario = currentScenario.get();
        if (scenario.isFailed() && !"api".equalsIgnoreCase(PropertyHelper.getProperty("testType"))) {
            captureScreenshotAndAttach(scenario);
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if ("api".equalsIgnoreCase(PropertyHelper.getProperty("testType"))){
            apiHelper.get().close();
        }else {
            pageHelper.get().close();
        }
    }

    public static PageHelper getPageManager() {
        return pageHelper.get();
    }

    public static ApiHelper getApiManager() {
        return apiHelper.get();
    }

    private void captureScreenshotAndAttach(Scenario scenario) {
        if (!"api".equalsIgnoreCase(PropertyHelper.getProperty("testType"))) {
            byte[] screenshot = getPageManager().getPage(Thread.currentThread().getName()).screenshot();
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
        }
    }

}
