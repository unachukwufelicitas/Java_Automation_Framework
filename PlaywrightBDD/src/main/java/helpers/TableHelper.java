package helpers;

import com.microsoft.playwright.Locator;

import java.util.HashMap;
import java.util.Map;

/*
    Java class (TableHelper) provides a method for locating a specific cell within an HTML table
    using Playwright locators. It encapsulates the logic for searching and navigating within the table structure,
    making it easier to work with tables in web automation scripts.
 */

public class TableHelper {

    public static Locator getTableCell(Locator table, String referenceRow, String columnHeader) throws InterruptedException {
        table.waitFor(); // wait for table to load

        // Header
        Locator tHead = table.locator("thead");
        Locator tHeadRow = tHead.locator("tr");
        Locator[] tHeaderColumns = tHeadRow.locator("th").all().toArray(new Locator[0]);

        // Body
        Locator tBody = table.locator("tbody");

        Locator[] tBodyRows = null;
        int countdown = 0;

        while (tBodyRows == null || countdown > 2000) {
            tBodyRows = tBody.locator("tr").all().toArray(new Locator[0]);
            countdown++;
            Thread.sleep(1000);
        }

        Map<String, Integer> columnHeaderMap = new HashMap<>();

        // Map column headers to their indexes
        for (int i = 0; i < tHeaderColumns.length; i++) {
            String columnHeaderText = tHeaderColumns[i].innerText().replace("\n", "");
            columnHeaderMap.put(columnHeaderText, i);
        }

        Integer referenceColumnIndex = columnHeaderMap.get(columnHeader);

        if (referenceColumnIndex == null) {
            System.out.println("Reference column header not found.");
            return null;
        }

        int referenceRowIndex = -1;
        int rowIndex = 0;

        // Find the index of the reference row
        for (Locator tBodyRow : tBodyRows) {
            String rowText = tBodyRow.innerText().replace("\n", "");
            if (rowText.contains(referenceRow)) {
                referenceRowIndex = rowIndex;
                break;
            }
            rowIndex++;
        }

        if (referenceRowIndex == -1) {
            System.out.println("Reference row not found.");
            return null;
        }

        return tBodyRows[referenceRowIndex].locator("td").nth(referenceColumnIndex);
    }

}
