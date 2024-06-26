package test.java.DrukmakoriSivatag;

import main.java.DrukmakoriSivatag.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class DamageFixPipeSteps {
    private Saboteur saboteur;
    private Pipe pipe;

    private Pump pump;
    private Plumber plumber;

    @Given("^Saboteur is  on a damageable pipe$")
    public void saboteur_is_on_a_damageable_pipe() {
        saboteur = new Saboteur();
        pipe = new Pipe();
        pipe.accept(saboteur);
        saboteur.setPipelineElement(pipe);
    }

    @Given("^Saboteur is  on a not damageable pipe$")
    public void saboteur_is_on_a_not_damageable_pipe() {
        saboteur = new Saboteur();
        pipe = new Pipe();
        pump = new Pump();
        plumber = new Plumber();

        pipe.addNeighbor(pump);
        pipe.accept(plumber);
        plumber.setPipelineElement(pipe);

        plumber.damagePipe();
        plumber.fix();

        plumber.move(0);

        pipe.accept(saboteur);
        saboteur.setPipelineElement(pipe);
    }

    @Given("^Plumber is  on a damaged pipe$")
    public void plumber_is_on_a_damaged_pipe() {
        plumber = new Plumber();
        pipe = new Pipe();

        pipe.setIsDamaged(true);
        pipe.accept(plumber);
        plumber.setPipelineElement(pipe);
    }

    @When("^Saboteur damages the pipe$")
    public void saboteur_damages_the_pipe() {
        saboteur.damagePipe();
    }

    @When("^Plumber fixes the pipe$")
    public void plumber_fixes_the_pipe() {
        plumber.fix();
    }

    @Then("^The pipe is damaged$")
    public void the_pipe_is_damaged() {
        boolean isDamaged = pipe.isDamaged();
        assertTrue(isDamaged);
    }

    @Then("^The pipe is not damaged$")
    public void the_pipe_is_not_damaged() {
        boolean isDamaged = pipe.isDamaged();
        assertFalse(isDamaged);
    }

    @Then("^FixedTime is greater than 0$")
    public void fixedTime_is_grater_than_0() {
        boolean fixTimeGraterThan0 = pipe.getFixedTime() > 0;
        assertTrue(fixTimeGraterThan0);
    }
}
