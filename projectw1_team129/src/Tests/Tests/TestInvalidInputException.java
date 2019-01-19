package Tests;

import Model.*;
import Model.Exceptions.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class TestInvalidInputException {

    private RegularTask task;
    private Event event;
    private ListOfItemSingleton lois;
    private File fileBad;
    private File fileGood;



    @BeforeEach
    public void runBefore() throws InvalidInputException {
        lois = ListOfItemSingleton.getInstance();
        fileBad = new File("/Users/zoega/IdeaProjects/projectw1_team129/bad.txt");
        fileGood = new File("/Users/zoega/IdeaProjects/projectw1_team129/loadableTest.txt");
        task = new RegularTask("test", "today", false, false);
        event = new Event("test", "UBC", "today", "09:00", false, false);
    }

    @Test
    public void testSetDueNothingThrown(){
        try {
            task.setDue("tomorrow");
            System.out.println("yay");

        }catch (InvalidInputException e) {
            fail("Caught exception when it wasn't supposed to be thrown");
        }
    }

    @Test
    public void testSetDueNothingThrown2(){
        try{
            task.setDue("Jan 5 2018");
            System.out.println("yay");

        } catch (InvalidInputException e) {
            fail("Caught exception when it wasn't supposed to be thrown");
        }

    }


    @Test
    public void testSetDueInvalidInputThrown(){
        try{
            task.setDue("blah");
            fail("Should've thrown exception");
        } catch (InvalidInputException e) {
            System.out.println("Yay");
        }
    }


    @Test
    public void testSetTimeNothingThrown(){
        try{
           event.setTime("18:00");
            System.out.println("Passed!");
        } catch(InvalidInputException e){
            fail("Shouldn't have caught that");
        }
    }

    @Test
    public void testSetTimeOutOfBoundsThrown(){
        try{
            event.setTime("70:00");
            fail("Should've caught the exception");
        } catch (InvalidInputException e){
            System.out.println("Passed!");
        }
    }


    @Test
    public void testSetTimeWrongInputThrown(){
        try{
            event.setTime("blahh");
            fail("should've caught the exception");
        } catch (InvalidInputException e){
            System.out.println("Passed!");
        }
    }

    @Test
    public void testLoadBadFileThrown() throws IOException{
        try {
            lois.load(fileBad);
            fail("Should've caught that exception.");
        } catch (InvalidInputException e) {
            System.out.println("Passed!");
        }

    }
}
