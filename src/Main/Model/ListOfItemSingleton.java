package Model;

import Model.Exceptions.EmptyListException;
import Model.Exceptions.InvalidInputException;
import Observer.Subject;
import Observer.Observer;
import ui.MainScreen;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ListOfItemSingleton extends Subject implements Loadable, Saveable, Observer {

    private static ListOfItemSingleton instance;
    private Map<String, Item> itemMap = new HashMap<>();

    private ListOfItemSingleton() {
//to ensure it cannot be instantiated outside of the class
        if (MainScreen.getMainMenu() != null) {
            Observer observer = MainScreen.getMainMenu();
            addObserver(observer);
        }
    }

    public static ListOfItemSingleton getInstance(){
        if(instance==null){
            instance = new ListOfItemSingleton();
        }
        return instance;
    }

    //MODIFIES: this
    //EFFECTS: adds item to map
    public void addItem(Item item){
        if (!itemMap.containsValue(item)) {
            itemMap.put(item.getName(), item);
            if (!(MainScreen.getMainMenu() == null)) {
                notifyObservers();
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: removes item
    public void removeItem(Item item) {
        if (itemMap.containsValue(item)) {
            itemMap.values().remove(item);
            if (!(MainScreen.getMainMenu() == null)) {
                notifyObservers();
            }
        }
    }

    //EFFECTS: prints each task in itemList
    public String printList() {
        String s = "";
        for (Map.Entry<String, Item> entry:  itemMap.entrySet()) {
            if (!s.equals("")) {
                s = s + "\n \n" + entry.getValue().printItem();
            }
            else {
                s = s + "\n" + entry.getValue().printItem();
            }
        }
        return s;
    }

    //MODIFIES: this
    //EFFECTS: returns itemList with only incomplete tasks in it
    public void getOnlyIncomplete() throws EmptyListException {
        if (itemMap.isEmpty()){
            throw new EmptyListException();
        }
        Set<String> keySet = new HashSet<>();

        for (Map.Entry<String, Item> entry:  itemMap.entrySet()){
            if (entry.getValue().getComplete()){
                keySet.add(entry.getKey());
            }
        }
        itemMap.keySet().removeAll(keySet);
    }

    //MODIFIES: this
    //returns itemList with only overdue tasks in it
    public void getOnlyOverdue() throws EmptyListException {
        if (itemMap.isEmpty()){
            throw new EmptyListException();
        }
        Set<String> keySet = new HashSet<>();

        for (Map.Entry<String, Item> entry:  itemMap.entrySet()){
            if (!entry.getValue().isOverdue()){
                keySet.add(entry.getKey());
            }
        }
        itemMap.keySet().removeAll(keySet);
    }

    //EFFECTS: returns itemMap
    public Map<String, Item> getMap() {return itemMap; }


    //EFFECTS: clears itemMap
    public void clearMap() {itemMap.clear();}


    //EFFECTS: saves itemList to file
    public void save(File file) throws IOException {
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        for (Map.Entry<String, Item> entry:  itemMap.entrySet()){
            entry.getValue().save(file);
        }
        writer.close();
    }

    //EFFECTS: creates itemList from file
    public void load(File file) throws IOException, InvalidInputException {
        List<String> taskLines = Files.readAllLines(Paths.get(file.getName()));
        for (String line: taskLines) {
            ArrayList<String> partsOfLine = splitOnComma(line);
            switch (partsOfLine.get(0)) {
                case "Regular":
                    Item regular = new RegularTask(partsOfLine.get(1), partsOfLine.get(2), Boolean.parseBoolean(partsOfLine.get(3)), Boolean.parseBoolean((partsOfLine.get(4))));
                    addItem(regular);
                    break;
                case "Academic":
                    String courseInfo = partsOfLine.get(2);
                    String department = courseInfo.substring(0, 4);
                    String code = courseInfo.substring(4);
                    Course course = new Course(department, code);
                    Item academic = new AcademicTask(partsOfLine.get(1), course, partsOfLine.get(3), Boolean.parseBoolean(partsOfLine.get(4)), Boolean.parseBoolean(partsOfLine.get(5)));
                    addItem(academic);
                    break;
                case "Event":
                    Item event = new Event(partsOfLine.get(1), partsOfLine.get(2), partsOfLine.get(3), partsOfLine.get(4), Boolean.parseBoolean(partsOfLine.get(5)), Boolean.parseBoolean((partsOfLine.get(6))));
                    addItem(event);
                    break;
                default:
                    throw new InvalidInputException();
            }
        }
        MainScreen.getMainMenu().setTitle();
    }

    //EFFECTS: splits line with a comma
    // this code is from the Saving/Loading deliverable
    public static ArrayList<String> splitOnComma(String line){
        String[] splits = line.split(",");
        return new ArrayList<>(Arrays.asList(splits));
    }

    @Override
    public void update() {
        notifyObservers();
    }
}
