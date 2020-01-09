package Tests;

import Model.Exceptions.DuplicateItemException;
import Model.Exceptions.InvalidInputException;
import Model.Item;
import Model.ListOfItemSingleton;
import Model.RegularTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.TaskManager;

import static org.junit.jupiter.api.Assertions.fail;

public class TestDuplicateItemException {

    private ListOfItemSingleton lois;
    private Item i1, i2, i3;

    @BeforeEach
    public void runBefore() throws InvalidInputException {
        lois = ListOfItemSingleton.getInstance();
        i1 = new RegularTask("test", "Sep 5 2020", false, false);
        i2 = new RegularTask("test", "Oct 9 2020", false, false);
        i3 = new RegularTask("Test", "Sep 4 2020", false, false);

        lois.addItem(i1);
    }


    @Test
    public void testAddDuplicateExpectThrown(){
        try {
            TaskManager ts = new TaskManager();
            ts.checkItem(i2);
            fail("should've caught that exception!");
        } catch (DuplicateItemException e) {
            System.out.println("Yeeeet");
        }
    }

    @Test
    public void testAddDifferentItemNothingThrown() {
        try {
            TaskManager ts = new TaskManager();
            ts.checkItem(i3);
            System.out.println("yeeet");
        } catch (DuplicateItemException e) {
            fail("shouldn't have caught the exception!");
        }
    }
}
