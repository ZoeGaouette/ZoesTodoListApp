package Tests;

import Model.*;
import Model.Exceptions.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.util.ArrayList;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadableTest {
    private File file;
    private ListOfItemSingleton lois;
    private Map<String, Item> todoList;
    private Item regular1;
    private Item academic1;
    private Item event1;


    @BeforeEach
    public void runBefore() throws IOException, InvalidInputException {
        regular1 = new RegularTask("Buy eggs", "Oct 5 2018", false, false);
        Course course = new Course("MATH", "217");
        academic1 = new AcademicTask("Do Homework 1", course, "Oct 10 2018", false, false);
        event1 = new Event("Swimming Lesson", "UBC Aquatic Centre", "Oct 14 2018", "02:00", false, false);
        file = new File("/Users/zoega/IdeaProjects/projectw1_team129/loadableTest.txt");
        System.out.println(file.getAbsolutePath());
        FileWriter fw = new FileWriter(file.getName(), false);
        fw.flush();
        fw.close();
        lois = ListOfItemSingleton.getInstance();
        todoList = lois.getMap();
        todoList.clear();
    }


    public void TestLoad(Loadable loadable) throws IOException, InvalidInputException {
        loadable.load(file);
    }


    //Test that one line from file is saved into memory (taskList)
    @Test
    public void loadOneLineTest() throws IOException, InvalidInputException {
        regular1.save(file);
        TestLoad(lois);
        BufferedReader br = new BufferedReader(new FileReader(file));
        assertEquals(todoList.get("Buy eggs").print(), br.readLine());
        assertEquals(todoList.size(), 1);
    }

    //Test that lines from file are saved into memory (taskList)
    @Test
    public void loadManyLinesTest() throws IOException, InvalidInputException {
        regular1.save(file);
        academic1.save(file);
        event1.save(file);
        TestLoad(lois);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            ArrayList<String> partsOfLine = ListOfItemSingleton.splitOnComma(line);
            assertEquals(todoList.get(partsOfLine.get(1)).print(), line);
        }
        assertEquals(3, todoList.size());
    }
}
