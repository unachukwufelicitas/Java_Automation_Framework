package steps;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import helpers.LocatorHelper;
import helpers.PageHelper;
import helpers.TableHelper;
import hooks.Hooks;
import io.cucumber.java.en.When;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;

public class TableDefinitions {

    private Page page;

    @When("^I click the \"(.*)\" (?:button|link) from the \"(.*)\" on the \"(.*)\" page, with a reference row of \"(.*)\"  and a column header of \"(.*)\"$")
    public void TableClickFunction(String textToClick, String key, String fileName, String referenceRow, String columnHeader) throws InterruptedException {

        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());

        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);

        /*
        loop is necessary in case a cell contains multiple element
        please make sure the tag of the element you want to click matches the below case
         */

        if (elementLocator.isVisible()) {

            Locator cell = TableHelper.getTableCell(elementLocator, referenceRow, columnHeader);
            assert cell != null;
            cell.highlight();

            if (cell.locator("button").first().isVisible()) {
                Locator[] buttonElements = cell.locator("button").all().toArray(new Locator[0]);
                for (Locator element : buttonElements) {
                    if (element.textContent().trim().equals(textToClick.trim())) {
                        element.highlight();
                        element.click();
                        page.waitForLoadState(LoadState.NETWORKIDLE); // Wait for network to be idle
                    }
                }
            } else {
                if (cell.locator("a").first().isVisible()) {
                    Locator[] linkElements = cell.locator("a").all().toArray(new Locator[0]);
                    for (Locator element : linkElements) {
                        if (element.textContent().trim().equals(textToClick.trim())) {
                            element.highlight();
                            element.click();
                            page.waitForLoadState(LoadState.NETWORKIDLE); // Wait for network to be idle
                        }
                    }
                } else {
                    if (cell.locator("label").first().isVisible()) {
                        Locator[] labelElements = cell.locator("label").all().toArray(new Locator[0]);
                        for (Locator element : labelElements) {
                            if (element.textContent().trim().equals(textToClick.trim())) {
                                element.highlight();
                                element.click();
                                page.waitForLoadState(LoadState.NETWORKIDLE); // Wait for network to be idle
                            }
                        }
                    } else {
                        if (cell.locator("span").first().isVisible()) {
                            Locator[] spanElements = cell.locator("span").all().toArray(new Locator[0]);
                            for (Locator element : spanElements) {
                                if (element.textContent().trim().equals(textToClick.trim())) {
                                    element.highlight();
                                    element.click();
                                    page.waitForLoadState(LoadState.NETWORKIDLE); // Wait for network to be idle
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @When("^on the \"(.*)\" with a reference row of \"(.*)\" and a column header of \"(.*)\", the (?:input-text|text-area|text-field) should be \"(.*)\", on the \"(.*)\" page$")
    public void TableTextFunction(String key, String referenceRow, String columnHeader, String value, String fileName) throws InterruptedException {

        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());

        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);

        /*
        loop is necessary in case a cell contains multiple element
        please make sure the tag of the element you want to click matches the below case
         */


        if (elementLocator.isVisible()) {
            Locator cell = TableHelper.getTableCell(elementLocator, referenceRow, columnHeader);
            assert cell != null;
            cell.highlight();

            if (cell.locator("input").first().isVisible()) {
                Locator[] elements = cell.locator("input").all().toArray(new Locator[0]);

                for (Locator element : elements) {
                    element.highlight();
                    if (!element.isEditable() || element.isDisabled()) {
                        String actualText = element.inputValue();
                        page.waitForLoadState(LoadState.NETWORKIDLE); // Wait for network to be idle
                        String expectedText = value.trim();

                        Assert.assertEquals("The values are not equal: ", expectedText, actualText);
                    }
                }
            }

            if (cell.locator("textarea").first().isVisible()) {
                Locator[] elements = cell.locator("textarea").all().toArray(new Locator[0]);

                for (Locator element : elements) {
                    element.highlight();
                    if (element.isDisabled() || !element.isEditable()) {
                        String actualText = element.inputValue();
                        page.waitForLoadState(LoadState.NETWORKIDLE); // Wait for network to be idle
                        String expectedText = value.trim();

                        Assert.assertEquals("The values are not equal: ", expectedText, actualText);
                    }
                }
            }
        }
    }


    @When("^I fill the \"(.*)\" (input|text-area|text-field) with \"(.*)\" from the on the \"(.*)\" page, with a reference row of \"(.*)\"  and a column header of \"(.*)\"$")
    public void TableFillFunction(String key, String fieldType, String value, String fileName, String referenceRow, String columnHeader) throws InterruptedException {

        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());

        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);

        /*
        loop is necessary in case a cell contains multiple element
        please make sure the tag of the element you want to click matches the below case
         */


        if (elementLocator.isVisible()) {
            switch (fieldType) {
                case "input" -> {
                    Locator input = TableHelper.getTableCell(elementLocator, referenceRow, columnHeader);
                    assert input != null;
                    input.highlight();
                    Locator[] elements = input.locator("input").all().toArray(new Locator[0]);

                    for (Locator element : elements) {
                        if (element != null && element.isVisible()) {
                            element.highlight();
                            element.clear();
                            element.type(value);
                            page.waitForLoadState(LoadState.NETWORKIDLE); // Wait for network to be idle
                        }
                    }
                }
                case "text area" -> {
                    Locator link = TableHelper.getTableCell(elementLocator, referenceRow, columnHeader);
                    assert link != null;
                    link.highlight();
                    Locator[] elements = link.locator("textarea").all().toArray(new Locator[0]);

                    for (Locator element : elements) {

                        element.highlight();
                        element.clear();
                        element.type(value);
                        page.waitForLoadState(LoadState.NETWORKIDLE); // Wait for network to be idle
                    }
                }
                default -> {
                    System.out.println("incorrect step must have been selected!!...accepted are: textarea, input");
                }
            }

        } else {
            System.out.println(elementLocator.innerHTML() + " not visible");
        }
    }

    @When("^I select the \"(.*)\" options from \"(.*)\" dropdown on the \"(.*)\" page, with a reference row of \"(.*)\"  and a column header of \"(.*)\"$")
    public void TableMultiSelectFunction(String options, String key, String fileName, String referenceRow, String columnHeader) throws InterruptedException {
        String[] arrayOptions = options.split(",\\s*");
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());

        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);

        if (elementLocator.isVisible()) {
            Locator cell = TableHelper.getTableCell(elementLocator, referenceRow, columnHeader);
            assert cell != null;
            Locator cellLocator = cell.locator("button");
            cellLocator.waitFor();
            cellLocator.highlight();
            cellLocator.click();
            page.waitForLoadState(LoadState.NETWORKIDLE); // Wait for network to be idle

            List<Locator> lists = new ArrayList<>(cell.locator("ul > li").all());

            for (String option : arrayOptions) {
                for (Locator list : lists) {

                    list.waitFor();
                    String actual = list.textContent().trim();
                    if (actual.equals(option.trim())) {
                        list.click();
                        break;
                    }
                }
            }

            cellLocator.waitFor();
            cellLocator.highlight();
            cellLocator.click();

        } else {
            System.out.println(elementLocator.innerHTML() + " not visible");
        }
    }

}
