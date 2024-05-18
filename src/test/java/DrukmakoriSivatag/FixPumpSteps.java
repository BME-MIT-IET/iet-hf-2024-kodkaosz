package test.java.DrukmakoriSivatag;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.java.DrukmakoriSivatag.Plumber;
import main.java.DrukmakoriSivatag.Pump;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FixPumpSteps {
    private Pump pump;
    private Plumber plumber;
    @Given("^Plumber is  on a damaged pump$")
    public void plumber_is_on_a_damaged_pump(){
        plumber = new Plumber();
        pump = new Pump();

        pump.setIsDamaged(true);
        pump.accept(plumber);
        plumber.setPipelineElement(pump);
    }

    @When("^Plumber fixes the pump$")
    public void plumber_fixes_the_pump() {
        plumber.fix();
    }
    @Then("^The pump is not damaged$")
    public void the_pump_is_not_damaged()  {
        boolean isDamaged= pump.getIsDamaged();
        assertEquals(false, isDamaged);
    }
}
