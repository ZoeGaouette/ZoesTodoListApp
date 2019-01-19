package ui;

import Model.Exceptions.*;
import Model.ListOfItemSingleton;

import java.io.IOException;
import java.io.File;

public class MainScreen {

    private static MainMenu mm;
    private static File fileInst;

    public static void main(String[] args) throws IOException, InvalidInputException {
        mm = new MainMenu();
        MainScreen ms = new MainScreen();
        FileManager.setFile(fileInst);
        ms.initialLoad();
        mm.createAndShowGUI();
    }

    public MainScreen(){
        fileInst = new File("default_todo_list.txt");
    }

    public void initialLoad() throws IOException, InvalidInputException {
        ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
        lois.load(FileManager.file);
    }

    public static MainMenu getMainMenu() {
        return mm;
    }
}
