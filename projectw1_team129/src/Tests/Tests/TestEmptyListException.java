package Tests;

import Model.*;
import Model.Exceptions.EmptyListException;
import Model.Exceptions.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.TaskEditorManager;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class TestEmptyListException {

    private ListOfItemSingleton lois;
    private Map<String, Item> map;
    private Item i1;
    private Item i2;



    @BeforeEach
    public void runBefore() throws InvalidInputException {
        lois = ListOfItemSingleton.getInstance();
        map = lois.getMap();
        i1 = new RegularTask("test", "today", false,false);
        i2 = new Event("test2","UBC", "Jan 5 2020", "08:00", false, false);

    }

    @Test
    public void testGetOverdueThrown(){
        try {
            lois.getOnlyOverdue();
            fail("Should've caught exception");
        } catch (EmptyListException e) {
            System.out.println("yeet");
        }
    }

    @Test
    public void testGetIncompleteThrown(){
        try {
            lois.getOnlyIncomplete();
            fail("Should've caught the exception");
        } catch (EmptyListException e) {
            System.out.println("yeet");
        }
    }

    @Test
    public void testItemSearcherThrown() throws IOException{
        try {
            TaskEditorManager tem = new TaskEditorManager();
            tem.itemSearcher();
            fail("Should've caught the exception");
        } catch (EmptyListException e) {
            System.out.println("yeet");
        }
    }

}
