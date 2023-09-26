package steps;

import com.deque.html.axecore.playwright.*; // 1
import com.deque.html.axecore.results.AxeResults;
import com.deque.html.axecore.results.CheckedNode;
import com.microsoft.playwright.Page;
import hooks.Hooks;
import io.cucumber.java.en.When;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.ListIterator;

import static org.testng.Assert.assertEquals;

/*
     Java class (Accessibility) contains steps for injecting Axe, performing accessibility scans, and
     generating HTML reports for accessibility testing in Playwright-based automation scripts.
     It helps ensure web applications are accessible and compliant with accessibility standards.
 */

public class AccessibilityDefinitions {

    private Page page;

    @When("^I inject axe on the page$")
    public void AxeFunction() {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());

        AxeBuilder axeBuilder = new AxeBuilder(page)
                .withTags(Arrays.asList("wcag2a", "wcag2aa", "wcag21a", "wcag21aa"));

        AxeResults accessibilityScanResults = axeBuilder.analyze();

        // Check if violations were found
        if (!accessibilityScanResults.getViolations().isEmpty()) {
            // Generate the HTML report
            generateHTMLReport(accessibilityScanResults);
        } else {
            System.out.println("No violations found.");
        }

        assertEquals(accessibilityScanResults.getViolations(), Collections.emptyList());
    }




    public void generateHTMLReport(AxeResults accessibilityScanResults) {
        StringBuilder htmlReport = new StringBuilder("<html><head><style>" +
                "body { font-family: Arial, sans-serif; }" +
                "h1 { color: #333333; font-size: 24px; }" +
                "h2 { color: #666666; font-size: 20px; margin-top: 30px; }" +
                "h3 { color: #999999; font-size: 16px; margin-top: 20px; }" +
                ".section { margin-top: 30px; }" +
                ".subsection { margin-top: 20px; }" +
                ".description { color: #333333; font-size: 14px; margin-bottom: 10px; }" +
                ".details { color: #555555; font-size: 12px; margin-bottom: 5px; }" +
                "table { border-collapse: collapse; width: 100%; }" +
                "th { background-color: #f2f2f2; color: #333333; font-weight: bold; padding: 8px; text-align: left; }" +
                "td { border: 1px solid #dddddd; padding: 8px; }" +
                "</style></head><body><h1>Accessibility Report</h1>");

        // Error Message Section
        htmlReport.append("<div class='section'>");
        htmlReport.append("<h2>Page URL</h2>");
        htmlReport.append("<table>");
        htmlReport.append("<tr><th>URL</th><td>").append(accessibilityScanResults.getUrl()).append("</td></tr>");
        htmlReport.append("</table>");
        htmlReport.append("</div>");

        // Violations Section
        htmlReport.append("<div class='section'>");
        htmlReport.append("<h2>Violations : Count is ").append((long) accessibilityScanResults.getViolations().size()).append(" </h2>");
        htmlReport.append("<table>");
        htmlReport.append("<tr><th>ID</th><th>Description</th><th>Nodes</th><th>Element</th><th>Impact</th></tr>");
        for (var violation : accessibilityScanResults.getViolations()) {
            htmlReport.append("<tr>");
            htmlReport.append("<td>").append(violation.getId()).append("</td>");
            htmlReport.append("<td>").append(violation.getDescription()).append("</td>");
            for (CheckedNode node : violation.getNodes()) {
                if(node != null){
                    htmlReport.append("<td>").append(node.getFailureSummary()).append("</td>");
                    break;
                }
            }
            for (CheckedNode node : violation.getNodes()) {
                if(node != null){
                    htmlReport.append("<td>").append(node.getHtml()).append("</td>");
                    break;
                }
            }
            htmlReport.append("<td>").append(violation.getImpact()).append("</td>");
            htmlReport.append("</tr>");
        }
        htmlReport.append("</table>");
        htmlReport.append("</div>");

        // Incomplete Section
        htmlReport.append("<div class='section'>");
        htmlReport.append("<h2>Incomplete : Count is ").append((long) accessibilityScanResults.getIncomplete().size()).append(" </h2>");
        htmlReport.append("<table>");
        htmlReport.append("<tr><th>ID</th><th>Description</th><th>Nodes</th><th>Element</th><th>Impact</th></tr>");
        for (var incomplete : accessibilityScanResults.getIncomplete()) {
            htmlReport.append("<tr>");
            htmlReport.append("<td>").append(incomplete.getId()).append("</td>");
            htmlReport.append("<td>").append(incomplete.getDescription()).append("</td>");
            for (CheckedNode node : incomplete.getNodes()) {
                if(node != null){
                    htmlReport.append("<td>").append(node.getFailureSummary()).append("</td>");
                    break;
                }
            }
            for (CheckedNode node : incomplete.getNodes()) {
                if(node != null){
                    htmlReport.append("<td>").append(node.getHtml()).append("</td>");
                    break;
                }
            }
            htmlReport.append("<td>").append(incomplete.getImpact()).append("</td>");
            htmlReport.append("</tr>");
        }
        htmlReport.append("</table>");
        htmlReport.append("</div>");

        htmlReport.append("</table>");

        htmlReport.append("</body></html>");

        String filePath = "accessibility-report/accessibility_report.html";

        try {
            FileWriter reportFile = new FileWriter(filePath);
            reportFile.write(htmlReport.toString());
            reportFile.close();
            System.out.println("HTML report generated at: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to generate HTML report: " + e.getMessage());
        }
    }



}



