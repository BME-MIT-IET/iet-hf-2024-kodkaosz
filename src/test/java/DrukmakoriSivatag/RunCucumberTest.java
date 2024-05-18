package test.java.DrukmakoriSivatag;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
//@CucumberOptions(plugin = {"pretty"})
@CucumberOptions(features = "src/test/resources/DrukmakoriSivatag")
public class RunCucumberTest{
}