package Tests;

import Model.Exceptions.EmptyListException;
import Model.Exceptions.InvalidInputException;
import Model.Item;
import Model.ListOfItemSingleton;
import Model.RegularTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestListOfItemSingleton {


    private ListOfItemSingleton lois;
    private Map<String, Item> map;


    @BeforeEach
    public void runBefore() throws InvalidInputException {
        lois = ListOfItemSingleton.getInstance();
        Item regularTask1 = new RegularTask("Buy Groceries", "Oct 2 2022", false, false);
        lois.addItem(regularTask1);
        Item regularTask2 = new RegularTask("Test", "Sep 2 2018", true, false);
        lois.addItem(regularTask2);
        map = lois.getMap();
    }


    //Tests that calling getIncomplete on a map of all complete tasks will return an empty map
    @Test
    public void testGetIncompleteAllComplete() throws EmptyListException {
        map.get("Buy Groceries").setComplete(true);
        lois.getOnlyIncomplete();
        assertEquals(0, map.size());
    }


    //Test that calling getIncomplete on a map of all incomplete tasks will return same map
    @Test
    public void testGetIncompleteAllIncomplete() throws EmptyListException {
        map.get("Test").setComplete(false);
        lois.getOnlyIncomplete();
        assertEquals(2, map.size());
    }

    //Test that calling getIncomplete on a map of incomplete and complete tasks will return map with only incomplete tasks in it
    @Test
    public void testGetIncompleteSomeIncomplete() throws EmptyListException {
        lois.getOnlyIncomplete();
        assertEquals(1, map.size());
        assertFalse(map.get("Buy Groceries").getComplete());
    }


    //Test that calling getOverdue on a map of overdue tasks will return same map
    @Test
    public void testGetOverdueAllOverdue() throws InvalidInputException, EmptyListException {
        map.get("Buy Groceries").setDue("Sep 1 2017");
        lois.getOnlyOverdue();
        assertEquals(2, map.size());
        assertTrue(map.get("Buy Groceries").getOverdue());
        assertTrue(map.get("Test").getOverdue());
    }

    //Test that calling GetOverdue on a map of no overdue items will return an empty map
    @Test
    public void testGetOverdueNoneOverdue() throws InvalidInputException, EmptyListException {
        map.get("Test").setDue("Sep 1 2028");
        lois.getOnlyOverdue();
        assertEquals(0, map.size());
    }


    //Test that calling GetOverdue on a map with some overdue and some not overdue items returns a map with only the overdue items in it
    @Test
    public void testGetOverdueSomeOverdue() throws EmptyListException {
        lois.getOnlyOverdue();
        assertEquals(1, map.size());
        assertTrue(map.get("Test").getOverdue());
    }
}
