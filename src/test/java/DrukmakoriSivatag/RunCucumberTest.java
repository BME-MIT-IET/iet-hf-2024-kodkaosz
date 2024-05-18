package test.java.DrukmakoriSivatag;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/DrukmakoriSivatag", plugin = {"pretty"})
public class RunCucumberTest{
}