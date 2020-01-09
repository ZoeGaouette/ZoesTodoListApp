package ui;

import Model.*;
import Model.Event;
import Model.Exceptions.EmptyListException;
import Model.Exceptions.InvalidInputException;
import Model.Exceptions.OverdueItemException;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TaskEditorManager {

    public void itemSearcher() throws EmptyListException {
        while (true) {
            ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
            if (lois.getMap().isEmpty()) {
                throw new EmptyListException();
            }
            ListManager lm = new ListManager();
            try {
                lm.analyzeList(lois.getMap());
            } catch (OverdueItemException e) {
                lm.itemsOverdue();
            }

            ArrayList<String> itemNames = new ArrayList<>();
            itemNames.addAll(lois.getMap().keySet());
            Object[] items = itemNames.toArray();
            Map<String, Item> im = lois.getMap();
            JPanel panel = new JPanel();
            JComboBox<Object> options = new JComboBox<>(items);
            panel.add(new JLabel("Which item would you like to edit? "));
            panel.add(options);
            Set<Item> removed = new HashSet<>();
            int result = JOptionPane.showConfirmDialog(null, panel, "Edit Item",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE );
            if (result == JOptionPane.OK_OPTION){
                Item it = im.get(options.getSelectedItem().toString());
                if (it.getType().equals(Type.REGULARTASK)){
                    RegularTask rTask = (RegularTask) it;
                    regularItemEditor(rTask, removed);
                } else if (it.getType().equals(Type.ACADEMICTASK)){
                    AcademicTask aTask = (AcademicTask) it;
                    academicItemEditor(aTask, removed);
                } else if (it.getType().equals(Type.EVENT)){
                    Event eTask = (Event) it;
                    eventItemEditor(eTask,removed);
                }
            }
            else if(result == JOptionPane.CANCEL_OPTION){
                break;
            }

            for (Item item: removed){
                lois.removeItem(item);
            }
            panel.setVisible(false);
        }
    }

    public void regularItemEditor(RegularTask rTask, Set<Item> removed) {
        String[] items = {"Mark as complete", "Change the name", "Change the due date", "Remove it from your to-do list"};
        JComboBox<String> options = new JComboBox<>(items);
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Here is the item you wanted: " ));
        JTextArea itemPrinted = setItemJTextArea(rTask);
        panel.add(itemPrinted);
        panel.add(new JLabel("What would you like to do with it?"));
        panel.add(options);
        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Regular Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
            editorSearcher(options.getSelectedItem().toString(),rTask, removed);
        }
    }

    public void setNewItemName(Item item) {
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("New Name: "));
        JTextField nameField = new JTextField();
        panel.add(nameField);
        int result = JOptionPane.showConfirmDialog(null, panel, "Set New Item Name",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                item.setName(nameField.getText());
            } catch (InvalidInputException e) {
                MainMenu.playSoundException();
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again");
                setNewItemName(item);
            }
        }
    }

    public void setNewItemDate(Item item) {
        JPanel panel = new JPanel();
        JDatePickerImpl datePicker = TaskManager.createDatePicker();
        panel.add(new JLabel("New Due Date: "));
        panel.add(datePicker);
        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Date",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                item.setDue(TaskManager.changeFormat(datePicker.getJFormattedTextField().getText()));
            } catch (InvalidInputException e) {
                setNewItemDate(item);
            }
        }
    }

    public void academicItemEditor(AcademicTask aTask, Set<Item> removed){
        String[] items = {"Mark as complete", "Change the name", "Change the course", "Change the due date", "Remove it from your to-do list"};
        JComboBox<String> options = new JComboBox<>(items);
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Here is the item you wanted: \n"));
        JTextArea itemPrinted = setItemJTextArea(aTask);
        panel.add(itemPrinted);
        panel.add(new JLabel("What would you like to do with it?"));
        panel.add(options);
        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Academic Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
            editorSearcher(options.getSelectedItem().toString(),aTask, removed);
        }
    }


    public void setNewItemCourse(AcademicTask aTask) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField nameField = new JTextField();
        JTextField codeField = new JTextField();
        panel.add(new JLabel("Please enter the department name (4-character abbreviation, eg. MATH): "));
        panel.add(nameField);
        panel.add(new JLabel("Enter the course code: "));
        panel.add(codeField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Change course",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Course course = new Course(nameField.getText(), codeField.getText());
                aTask.setCourse(course);
            } catch (InvalidInputException e) {
                JOptionPane.showMessageDialog(null, "Invalid Input. Please try again.");
                MainMenu.playSoundException();
                setNewItemCourse(aTask);
            }
        }
    }


    public void eventItemEditor(Event eTask, Set<Item> removed){
        String[] items = {"Mark as complete", "Change the name", "Change the location", "Change the date", "Change the time",
                "Remove it from your to-do list"};
        JComboBox<String> options = new JComboBox<>(items);
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Here is the item you wanted: "));
        JTextArea itemPrinted = setItemJTextArea(eTask);
        panel.add(itemPrinted);
        panel.add(new JLabel("What would you like to do with it?"));
        panel.add(options);
        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Event Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
            editorSearcher(Objects.requireNonNull(options.getSelectedItem()).toString(),eTask, removed);
        }
    }


    public void setNewItemLocation(Event eTask) {
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("New Location: "));
        JTextField locationField = new JTextField();
        panel.add(locationField);
        int result = JOptionPane.showConfirmDialog(null, panel, "Change Location",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION){
            try {
                eTask.setLocation(locationField.getText());
            } catch (InvalidInputException e) {
                MainMenu.playSoundException();
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
                setNewItemLocation(eTask);
            }
        }
    }

    public void setNewItemTime(Event eTask) {
        String tt = JOptionPane.showInputDialog("Please enter the new time of this event, in the form HH:mm");
        try {
            eTask.setTime(tt);
        }catch (InvalidInputException e){
            MainMenu.playSoundException();
            JOptionPane.showMessageDialog(null, "Invalid input, please try again.");
            setNewItemTime(eTask);
        }
    }


    public void editorSearcher(String itemSearch, Item item, Set<Item> removed) {
            if (itemSearch.equals("Mark as complete")) {
                item.setComplete(true);
            } else if (itemSearch.equals("Change the name")) {
                setNewItemName(item);
            } else if ((itemSearch.equals("Change the course")) && (item instanceof AcademicTask)) {
                AcademicTask aTask = (AcademicTask) item;
                setNewItemCourse(aTask);
            } else if (itemSearch.equals("Change the location") && (item instanceof Event)) {
                Event eTask = (Event) item;
                setNewItemLocation(eTask);
            } else if (itemSearch.equals("Change the date")||itemSearch.equals("Change the due date")) {
                setNewItemDate(item);
            } else if ((itemSearch.equals("Change the time")) && (item instanceof Event)) {
                Event eTask = (Event) item;
                setNewItemTime(eTask);
            } else if (itemSearch.equals("Remove it from your to-do list")) {
                removed.add(item);
            }
        }


        public JTextArea setItemJTextArea(Item item){
        JTextArea itemPrinted = new JTextArea(item.printItem());
        itemPrinted.setEditable(false);
        return itemPrinted;
        }
}
