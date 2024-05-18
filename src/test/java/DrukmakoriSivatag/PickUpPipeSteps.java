package test.java.DrukmakoriSivatag;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.java.DrukmakoriSivatag.Pipe;
import main.java.DrukmakoriSivatag.Plumber;
import main.java.DrukmakoriSivatag.Pump;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PickUpPipeSteps {
    private Plumber plumber;
    private Pump pump1;
    private Pump pump2;
    private Pump pump3;
    private Pipe pipe1;
    private Pipe pipe2;

    private boolean connectSuccess;
    private boolean pickUpSuccess;

    @Given("^Plumber is  on a pump$")
    public void plumber_is_on_a_pump(){
        plumber = new Plumber();
        pump1 = new Pump();
        pump2 = new Pump();
        pump3 = new Pump();
        pipe1 = new Pipe();
        pipe2 = new Pipe();

        pipe1.addNeighbor(pump1);
        pipe1.addNeighbor(pump2);

        pipe2.addNeighbor(pump1);
        pipe2.addNeighbor(pump3);

        pump1.accept(plumber);
        plumber.setPipelineElement(pump1);
    }
    @Given("^Plumber moves to another pump$")
    public void plumber_moves_to_another_pump(){
        plumber.move(0);
        plumber.move(0);
    }


    @When("^Plumber picks up pipe$")
    public void plumber_picks_up_pipe() {
        pickUpSuccess=plumber.pickUpPipeEnd(0);
    }

    @When("^Plumber connects pipe$")
    public void plumber_connects_pipe() {
        connectSuccess=plumber.connectPipeEnd();
    }
    @Then("^The pick up was successful$")
    public void the_pickup_was_successful()  {
        assertEquals(true, pickUpSuccess);
    }

    @Then("^The pick up was not successful$")
    public void the_pickup_was_not_successful()  {
        assertEquals(false, pickUpSuccess);
    }

    @Then("^The connection was successful$")
    public void the_connection_not_successful()  {
        assertEquals(true, connectSuccess);
    }

    @Then("^Pipe1 is picked up$")
    public void pipe1_is_picked_up()  {
        assertEquals(true, pipe1.getIsPickedUp());
    }

    @Then("^Pipe2 is picked up$")
    public void pipe2_is_picked_up()  {
        assertEquals(true, pipe2.getIsPickedUp());
    }

    @Then("^Pipe1 is not picked up$")
    public void pipe1_is_not_picked_up()  {
        assertEquals(false, pipe1.getIsPickedUp());
    }

    @Then("^Plumber has a pipe$")
    public void plumber_has_a_pipe()  {
        boolean plumberHasAPipe = plumber.pickedUpPipe!=null;
        assertEquals(true, plumberHasAPipe);
    }

    @Then("^Plumber has no pipe$")
    public void plumber_has_no_pipe()  {
        boolean plumberHasAPipe = plumber.pickedUpPipe!=null;
        assertEquals(false, plumberHasAPipe);
    }
}
