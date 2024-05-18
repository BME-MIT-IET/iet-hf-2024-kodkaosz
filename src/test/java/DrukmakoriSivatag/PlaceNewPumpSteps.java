package test.java.DrukmakoriSivatag;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.java.DrukmakoriSivatag.Pipe;
import main.java.DrukmakoriSivatag.Plumber;
import main.java.DrukmakoriSivatag.Pump;
import main.java.DrukmakoriSivatag.WaterTank;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceNewPumpSteps {
    private Plumber plumber;
    private Pump pump;
    private Pump newPump;
    private Pipe pipe;

    private WaterTank waterTank;

    private boolean connectSuccess;
    private boolean pickUpSuccess;

    @Given("^Plumber is  on the watertank$")
    public void plumber_is_on_the_watertank() {
        plumber = new Plumber();
        pump = new Pump();
        pipe = new Pipe();
        waterTank = new WaterTank();

        pipe.addNeighbor(pump);
        pipe.addNeighbor(waterTank);

        waterTank.accept(plumber);
        plumber.setPipelineElement(waterTank);
    }

    @Given("^Plumber moves to a pipe$")
    public void plumber_moves_to_a_pipe() {
        plumber.move(0);
    }


    @When("^Plumber picks up pump$")
    public void plumber_picks_up_pump() {
        pickUpSuccess = plumber.pickUpPump();
        newPump = plumber.pickedUpPump;
    }

    @When("^Plumber connects pump$")
    public void plumber_connects_pump() {
        connectSuccess = plumber.addPump();
    }

    @Then("^The pump pick up was successful$")
    public void the_pump_pickup_was_successful() {
        assertTrue(pickUpSuccess);
    }

    @Then("^The pump pick up was not successful$")
    public void the_pump_pickup_was_not_successful() {
        assertFalse(pickUpSuccess);
    }

    @Then("^The pump connection was successful$")
    public void the_pump_connection_was_successful() {
        assertTrue(connectSuccess);
    }

    @Then("^The pump connection was not successful$")
    public void the_pump_connection_was_not_successful() {
        assertFalse(connectSuccess);
    }

    @Then("^Pump is picked up$")
    public void pump_is_picked_up() {
        assertTrue(newPump.getIsPickedUp());
    }


    @Then("^Pump is not picked up$")
    public void pump_is_not_picked_up() {
        assertFalse(newPump.getIsPickedUp());
    }

    @Then("^Plumber has a pump$")
    public void plumber_has_a_pump() {
        boolean plumberHasAPipe = plumber.pickedUpPump != null;
        assertTrue(plumberHasAPipe);
    }

    @Then("^Plumber has no pump$")
    public void plumber_has_no_pump() {
        boolean plumberHasAPipe = plumber.pickedUpPump != null;
        assertFalse(plumberHasAPipe);
    }
}
