package Tests;


import Model.*;
import Model.Exceptions.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.util.ArrayList;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class SaveableTest {
    private File file;
    private ListOfItemSingleton lois;
    private Map<String, Item> todoList;
    private FileWriter fw;
    private Item regular1;
    private Item academic1;
    private Item event1;
    private Course course;


    @BeforeEach
    public void runBefore() throws IOException, InvalidInputException {
        regular1 = new RegularTask("test", "today", false, false);
        course = new Course("CPSC", "210");
        academic1 = new AcademicTask("Study for midterm", course, "Oct 12 2018", false, false);
        event1 =new Event("Homecoming", "UBC Football field", "Oct 7 2018", "03:00", false, false);
        file = new File("/Users/zoega/IdeaProjects/projectw1_team129/saveableTest.txt");
        fw = new FileWriter(file.getName(), false);
        fw.flush();
        fw.close();
        lois = ListOfItemSingleton.getInstance();
        todoList = lois.getMap();
    }


    public void TestSave(Saveable saveable) throws IOException{
        saveable.save(file);
    }

    //Test that item is saved into file in proper format
    @Test
    public void saveOneRegTaskTest() throws IOException{
        todoList.clear();
        todoList.put("test", regular1);
        TestSave(lois);
        BufferedReader br = new BufferedReader(new FileReader(file));
        assertEquals(regular1.print(), br.readLine());

    }

    //Test that multiple tasks are saved into file in proper format
    @Test
    public void saveMultipleRegTasksTest() throws IOException, InvalidInputException {
        Item regular2 = new RegularTask("test2", "tomorrow", false, false);
        todoList.put("test", regular1);
        todoList.put("test2", regular2);
        TestSave(lois);
        fileReader();

    }

    //Test that one academic task is saved to file
    @Test
    public void saveOneAcadTaskTest() throws IOException{
        todoList.clear();
        todoList.put("Study for midterm", academic1);
        TestSave(lois);
        BufferedReader br = new BufferedReader(new FileReader(file));
        assertEquals(academic1.print(), br.readLine());
    }

    //Test that multiple academic tasks are saved to file
    @Test
    public void saveMultipleAcadTaskTest() throws IOException, InvalidInputException {
        Item academic2 = new AcademicTask("Study for midterm 2", course, "Oct 12 2018", false, false);
        todoList.put("Study for midterm", academic1);
        todoList.put("Study for midterm 2", academic2);
        TestSave(lois);
        fileReader();
    }

    //Test that one event is saved to file
    @Test
    public void saveOneEventTest() throws IOException{
        todoList.clear();
        todoList.put("Homecoming", event1);
        TestSave(lois);
        BufferedReader br = new BufferedReader(new FileReader(file));
        assertEquals(event1.print(), br.readLine());

    }

    //Test that multiple events are saved to file
    @Test
    public void saveMultipleEventsTest() throws IOException, InvalidInputException {
        Item event2 = new Event("Birthday Party", "Walter Gage: Floor 6", "Oct 1 2018", "19:00", false, false);
        todoList.put("Homecoming", event1);
        todoList.put("Birthday Party", event2);
        TestSave(lois);
        fileReader();

    }

    //Test that items of different types are saved to file
    @Test
    public void saveAllEventsTest() throws IOException {
        todoList.put("test", regular1);
        todoList.put("Study for midterm", academic1);
        todoList.put("Homecoming", event1);
        TestSave(lois);
        fileReader();

    }

    public void fileReader() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            ArrayList<String> partsOfLine = ListOfItemSingleton.splitOnComma(line);
            assertEquals(todoList.get(partsOfLine.get(1)).print(), line);
        }

    }



}
