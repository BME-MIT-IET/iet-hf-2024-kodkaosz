package test.java.DrukmakoriSivatag;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.java.DrukmakoriSivatag.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveToFreeSteps {
    private Saboteur saboteur;
    private WaterTank waterTank;
    private WaterSource waterSource;
    private Desert desert;
    private Pump pump;
    private Pipe pipe1;
    private Pipe pipe2;
    private Pipe pipe3;
    private PipelineElement from;
    private PipelineElement to;

    @Given("^Saboteur is  on the watertank$")
    public void saboteur_is_on_the_watertank(){
        saboteur = new Saboteur();
        waterSource = new WaterSource();
        waterTank = new WaterTank();
        desert = new Desert();
        pipe1 = new Pipe();
        pipe2 = new Pipe();
        pipe3 = new Pipe();
        pump = new Pump();

       pipe1.addNeighbor(pump);
       pipe1.addNeighbor(waterTank);

       pipe2.addNeighbor(pump);
       pipe2.addNeighbor(desert);

       pipe3.addNeighbor(pump);
       pipe3.addNeighbor(waterSource);

        waterTank.accept(saboteur);
        saboteur.setPipelineElement(waterTank);
    }

    @When("^Saboteur moves to pipe1$")
    public void saboteur_moves_to_pipe1() {
        from = saboteur.getPipelineElement();
        saboteur.move(0);
        to = saboteur.getPipelineElement();
    }
    @When("^Saboteur moves to pump$")
    public void saboteur_moves_to_pump() {
        from = saboteur.getPipelineElement();
        saboteur.move(0);
        to = saboteur.getPipelineElement();
    }

    @When("^Saboteur moves to pipe2$")
    public void saboteur_moves_to_pipe2() {
        from = saboteur.getPipelineElement();
        saboteur.move(1);
        to= saboteur.getPipelineElement();
    }
    @When("^Saboteur moves to desert$")
    public void saboteur_moves_to_desert() {
        from = saboteur.getPipelineElement();
        saboteur.move(1);
        to= saboteur.getPipelineElement();
    }

    @When("^Saboteur moves to pipe3$")
    public void saboteur_moves_to_pipe3() {
        from = saboteur.getPipelineElement();
        saboteur.move(2);
        to= saboteur.getPipelineElement();
    }
    @When("^Saboteur moves to watersource$")
    public void saboteur_moves_to_watersource() {
        from = saboteur.getPipelineElement();
        saboteur.move(1);
        to= saboteur.getPipelineElement();
    }

    @Then("^Move was successful$")
    public void move_was_successful()  {
        boolean success = from!=to;
        assertEquals(true, success);
    }

    @Then("^Move was not successful$")
    public void move_was_not_successful()  {
        boolean success = from!=to;
        assertEquals(false, success);
    }
}
