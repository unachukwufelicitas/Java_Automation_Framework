package steps;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;
import hooks.Hooks;
import io.cucumber.java.en.When;

/*
    Java class (WaitDefinitions) contains Cucumber step definitions and a utility method for various wait actions,
    including waiting for network activity, waiting for page load, waiting for DOM content load,
    and introducing a delay in the test execution.
    It helps control the timing of actions in Playwright-based automation scripts.
 */
public class WaitDefinitions {
    private Page page;

    @When("^I wait for network to load$")
    public void WaitForNetwork() {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());
        page.waitForLoadState(LoadState.NETWORKIDLE); // Wait for network to be idle
    }

    @When("^I wait for page to load$")
    public void WaitForPage() {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());
        page.waitForLoadState(LoadState.LOAD); // Wait for page to be idle
    }

    @When("^I wait for dom content to load$")
    public void WaitForDOM() {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());
        page.waitForLoadState(LoadState.DOMCONTENTLOADED); // Wait for dom to be idle
    }


    @When("^I wait for (\\d+) seconds$")
    public void waitForSeconds(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L); // Convert seconds to milliseconds
    }

    public static void waitForLoadStateWithRetry(Page page, LoadState loadState, int maxRetries, int delay) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                page.waitForLoadState(loadState);
                return; // Load state reached, exit the loop
            } catch (PlaywrightException e) {
                System.out.println("Load state not reached, retrying in " + (delay / 1000) + " seconds...");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        System.out.println("Load state not reached within the specified time.");
    }
}
