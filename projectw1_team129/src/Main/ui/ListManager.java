package ui;

import Model.Exceptions.EmptyListException;

import Model.Exceptions.InvalidInputException;
import Model.Exceptions.OverdueItemException;
import Model.Item;
import Model.ListOfItemSingleton;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ListManager {

    private ArrayList<Item> overdueItems = new ArrayList<>();

    public void listInteractor() throws IOException {
        while (true) {
            ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
            try {
                analyzeList(lois.getMap());
            } catch (OverdueItemException e) {
                itemsOverdue();
            }
            String i = JOptionPane.showInputDialog("\n What would you like to do now? \n (A) Add more items \n (G) Get a list of only incomplete items \n (O) Get a list of only overdue items \n (E) Edit items \n " +
                    " \n (M) Return to the main menu");
            if (i.equals("A")) {
                TaskManager tm = new TaskManager();
                tm.itemAdder();
            } else if (i.equals("G")) {
                try {
                    lois.getOnlyIncomplete();
                    JOptionPane.showMessageDialog(null, "I have removed all complete items from your list");
                    MainScreen.getMainMenu().setCurrentList();
                } catch (EmptyListException e) {
                    JOptionPane.showMessageDialog(null, "Your list is empty!");
                }
            } else if (i.equals("O")) {
                try {
                    lois.getOnlyOverdue();
                    JOptionPane.showMessageDialog(null, "Your list now contains only overdue items");
                } catch (EmptyListException e) {
                    JOptionPane.showMessageDialog(null, "Your list is empty!");
                }
            } else if (i.equals("E")) {
                try {
                    TaskEditorManager tem = new TaskEditorManager();
                    tem.itemSearcher();
                } catch (EmptyListException e) {
                    JOptionPane.showMessageDialog(null, "Your list is empty!");
                }
            } else if (i.equals("M")) {
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid selection.");
                System.out.println("Invalid Selection.");
            }
        }
    }

    public void itemsOverdue() {
        MainMenu.playSoundException();
        JOptionPane.showMessageDialog(null, "One or more items in your list are overdue." + "\n");
        ArrayList<Item> remove = new ArrayList<>();
        ArrayList<Item> removeFromList = new ArrayList<>();
        for (Item item : overdueItems) {
            JPanel panel = new JPanel(new GridLayout(0,1));
            String[] items = {"Change the due date", "Remove from To-Do List", "Leave as is"};
            JComboBox<String> options = new JComboBox<>(items);
            panel.add(new JLabel("Here is an overdue item: \n" ));
            JTextArea area = new JTextArea(item.printItem());
            area.setEditable(false);
            panel.add(area);
            panel.add(new JLabel("What would you like to do? \n"));
            panel.add(options);

            int result = JOptionPane.showConfirmDialog(null, panel, "Overdue Item",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                if (options.getSelectedIndex() == 0) {
                    TaskEditorManager tem = new TaskEditorManager();
                    tem.setNewItemDate(item);
                    remove.add(item);
                } else if (options.getSelectedIndex() == 1) {
                    removeFromList.add(item);
                    remove.add(item);
                } else if (options.getSelectedIndex() == 2) {
                    item.setLeaveAsIs(true);
                    remove.add(item);
                }
            }
        }
        overdueItems.removeAll(remove);
        ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
        for (Item item : removeFromList) {
            lois.removeItem(item);
        }
    }

    public void analyzeList(Map<String, Item> map) throws OverdueItemException {
        for (Map.Entry<String, Item> entry : map.entrySet()) {
            if (entry.getValue().isOverdue() && !entry.getValue().getLeaveAsIs()) {
                overdueItems.add(entry.getValue());
            }
        }
        if (!(overdueItems.size() == 0)) {
            throw new OverdueItemException();
        }
    }

    public void listCreator() throws IOException {
        JPanel panel = new JPanel(new GridLayout(0,1));
        JLabel input = new JLabel("What would you like the name of the list to be? (please use underscores instead of spaces)");
        panel.add(input);
        JTextField nameField = new JTextField();
        panel.add(nameField);
        int result = JOptionPane.showConfirmDialog(null, panel, "Create new To-do List",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                FileManager fm = new FileManager();
                checkListName(nameField.getText());
                fm.createNewFile(nameField.getText());
                MainScreen.getMainMenu().setCurrentList();
                MainScreen.getMainMenu().setTitle();
                JOptionPane.showMessageDialog(null, "Okay, I've create a to-do list for you!");
                TaskManager tm = new TaskManager();
                tm.itemAdder();
            } catch (InvalidInputException e) {
                MainMenu.playSoundException();
                JOptionPane.showMessageDialog(null, "Invalid name. The name cannot be blank, and must have underscores (_), instead of spaces");
            }
        }
    }

    public void checkListName(String name) throws InvalidInputException {
        if ((name == null)||(name.equals(""))){
            throw new InvalidInputException();
        }
        ArrayList<String> characters = new ArrayList<>(Arrays.asList(name.split("")));

        if(characters.contains(" ")){
            throw new InvalidInputException();
        }

    }
}
