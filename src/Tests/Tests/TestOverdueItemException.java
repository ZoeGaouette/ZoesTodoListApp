package Tests;

import Model.*;
import Model.Exceptions.InvalidInputException;
import Model.Exceptions.OverdueItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ListManager;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class TestOverdueItemException {
    private ListOfItemSingleton lois;
    private Map<String, Item> map;
    private Item i1;
    private Item i2;
    private Item i3;

    @BeforeEach
    public void runBefore() throws InvalidInputException {
        lois = ListOfItemSingleton.getInstance();
        map = lois.getMap();
        i1 = new RegularTask("test","Jan 5 2022", false, false);
        i2 = new RegularTask("test2", "Jan 5 2000", false, false);
        i3 = new RegularTask("test3", "Jan 5 2000", false, true);

    }


    @Test
    public void testAnalyzeListNothingThrown(){
        map.put(i1.getName(), i1);
        try {
            ListManager lm = new ListManager();
            lm.analyzeList(map);
            System.out.println("yeeet");
        }
        catch (OverdueItemException e){
            fail("shouldn't have caught it");
        }
    }

    @Test
    public void testAnalyzeListOneOverdue(){
        map.put(i1.getName(), i1);
        map.put(i2.getName(), i2);
        try{
            ListManager lm = new ListManager();
            lm.analyzeList(map);
            fail("should've caught the exception");
        }catch (OverdueItemException e){
            System.out.println("yeet");
        }
    }

    @Test
    public void testAnalyzeListOneOverdueLeaveAsIs(){
        map.put(i3.getName(), i3);
        try{
            ListManager lm = new ListManager();
            lm.analyzeList(map);
            System.out.println("yeet");
        } catch (OverdueItemException e){
            fail("Shouldn't have caught it");
        }
    }
}
