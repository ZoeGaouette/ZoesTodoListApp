package Tests;

import Model.Exceptions.InvalidInputException;
import Model.Item;
import Model.RegularTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class TestItem {

    private Item overdueItem;
    private Item notOverdueItem;

    @BeforeEach
    public void runBefore() throws InvalidInputException {
        overdueItem = new RegularTask("Buy Eggs", "Sep 7 2017", false, false);
        notOverdueItem = new RegularTask("Buy Eggs", "Sep 7 2027", false, false);
    }

    //Test if item with due date that is overdue is overdue
    @Test
    public void testIsOverdueOverdueTask() {
        assertTrue(overdueItem.isOverdue());
    }

    //Test if item with due date that is not overdue is not overdue
    @Test
    public void testIsOverdueNotOverdueTask() {
        assertFalse(notOverdueItem.isOverdue());

    }

    //Test that item that is overdue will be printed out as "yes"
    @Test
    public void testGetOverduePrintIsOverdue() {
        assertEquals("yes", overdueItem.getOverduePrint());
    }

    //Test that item that is not overdue will be printed out as "no"
    @Test
    public void testGetOverduePrintIsNotOverdue() {
        assertEquals("no", notOverdueItem.getOverduePrint());
    }

    //Test that item that is complete will be printed out as "complete"
    @Test
    public void testGetCompletePrintIsComplete() {
        overdueItem.setComplete(true);
        assertEquals("Complete", overdueItem.getCompletePrint());
    }

    //Test that item that is incomplete will be printed out as "incomplete"
    @Test
    public void testGetCompletePrintIsIncomplete() {
        assertEquals("Incomplete", overdueItem.getCompletePrint());
    }

}
