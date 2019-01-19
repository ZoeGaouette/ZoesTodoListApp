package Tests;



import Model.Exceptions.InvalidInputException;
import Model.Item;
import Model.Printing;
import Model.RegularTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrintingTest {

    private Item item1;
    private Item item2;

    @BeforeEach
    public void runBefore() throws InvalidInputException {
        item1 = new RegularTask("Test", "Oct 3 2018", false, false);
        item2 = new RegularTask("Test2", "Oct 3 2018", false, false);


    }

    public String printTest(Printing printing){
        return printing.print();

    }

    @Test
    public void printOneTaskTest(){
        assertEquals("Regular,Test,Oct 3 2018,false,false", printTest(item1));

    }



}
