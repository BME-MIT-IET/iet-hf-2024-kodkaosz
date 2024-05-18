package test.java.DrukmakoriSivatag;

import main.java.DrukmakoriSivatag.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DamagePipeDamageableSteps {
    private Saboteur saboteur;
    private Pipe pipe;
    @Given("^Saboteur is  on a damageable pipe$")
    public void saboteur_is_on_a_damageable_pipe(){
        saboteur = new Saboteur();
        pipe = new Pipe();
        pipe.accept(saboteur);
        saboteur.setPipelineElement(pipe);
    }
    @When("^Saboteur damages the pipe$")
    public void saboteur_damages_the_pipe() {
        saboteur.damagePipe();
    }
    @Then("^The pipe is damaged$")
    public void the_pipe_is_damaged()  {
        boolean isDamaged= pipe.isDamaged();
        assertEquals(true, isDamaged);
    }
}
