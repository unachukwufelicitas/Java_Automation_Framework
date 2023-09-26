package runner; //This class is in the "runner" package.

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/*
    Java class is a TestNG test runner specifically designed for running Cucumber tests. It specifies the location of
    feature files, step definitions, and other configuration options, and it enables parallel execution of Cucumber
    scenarios. The results are reported in both console output and HTML reports.
 */

//        features: Specifies the location of the feature files (BDD scenarios) to be executed. In this case, they are located in the "src/test/resources/features" directory.
//        glue: Defines the package(s) where the step definitions and hooks are located. In this example, they are in the "steps" and "hooks" packages.
//        plugin: Specifies the output format and location for Cucumber reports. In this case, it generates "pretty" console output and HTML reports in the "target/cucumber-reports" directory.
//        tags: Specifies which scenarios should be executed based on tags. In this case, scenarios with the "@parallel" tag are selected.
//        publish: This option is set to true, indicating that the test results should be published.
//        monochrome: When set to true, it ensures that console output is in a more readable format.

@CucumberOptions( //This annotation is used to configure Cucumber test options.
        features = "src/test/resources/features",
        glue = {"steps", "hooks"},
        plugin = {"pretty", "html:target/cucumber-reports"},
        tags = "@parallel",
        publish = true,
        monochrome = true
)

public class TestRunner extends AbstractTestNGCucumberTests{
    @Override
    @DataProvider(parallel = true) //This annotation configures a TestNG data provider for parallel execution.
    public Object[][] scenarios(){
        /*
            This method is overridden from the parent class (AbstractTestNGCucumberTests) and returns the scenarios to
            be executed. In this case, it returns scenarios obtained from the parent class for parallel execution.
         */
        return super.scenarios();
    }
}
