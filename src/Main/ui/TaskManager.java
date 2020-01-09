package ui;

import Model.*;
import Model.Event;
import Model.Exceptions.DuplicateItemException;
import Model.Exceptions.InvalidInputException;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class TaskManager {


    private boolean done;


    public void itemAdder() throws IOException {
        this.done = false;
        while (!done) {
            String[] items = {"Regular Task", "Academic Task", "Event"};
            JComboBox<String> options = new JComboBox<>(items);
            JPanel panel = new JPanel();
            panel.add(new JLabel("What type of item would you like to add to your list?"));
            panel.add(options);
            int result = JOptionPane.showConfirmDialog(null, panel, "Add an item",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String type = options.getSelectedItem().toString();
                if (type.equals("Regular Task")) {
                    addRegularTask();
                } else if (type.equals("Academic Task")) {
                    addAcademicTask();
                } else if (type.equals("Event")) {
                    addEvent();
                }
            }
            if (result == JOptionPane.CANCEL_OPTION) {
                this.done = true;
            }
        }
    }

    public void addRegularTask() throws IOException {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField nameField = new JTextField();
        panel.add(new JLabel("Name: "));
        panel.add(nameField);
        panel.add(new JLabel("Due Date: "));
        JDatePickerImpl datePicker = createDatePicker();
        panel.add(datePicker);
        int result = JOptionPane.showConfirmDialog(null, panel, "Add Regular Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Item item = new RegularTask(nameField.getText(), changeFormat(datePicker.getJFormattedTextField().getText()), false, false);
                checkAddSave(item);
                areYouFinished();
            } catch (InvalidInputException | DuplicateItemException e) {
                if (e instanceof InvalidInputException) {
                    MainMenu.playSoundException();
                    JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
                } else {
                    MainMenu.playSoundException();
                    JOptionPane.showMessageDialog(null, "Sorry, we already have an item with the name " + nameField.getText() +
                            ". Please try a different name.");
                }
                addRegularTask();
            }
        }
    }

    public void addAcademicTask() throws IOException {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField nameField = new JTextField();
        JTextField codeField = new JTextField();
        panel.add(new JLabel("Course Department Name (4 character abbreviation, eg. CPSC): "));
        panel.add(nameField);
        panel.add(new JLabel("Course code: "));
        panel.add(codeField);
        int result = JOptionPane.showConfirmDialog(null, panel, "Specify Course",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Course course = new Course(nameField.getText(), codeField.getText());
                addAcademicTask(course);
                areYouFinished();
            } catch (InvalidInputException e) {
                MainMenu.playSoundException();
                JOptionPane.showMessageDialog(null, "Invalid input. Try Again.");
                addAcademicTask();
            } catch (NullPointerException e) {
                MainMenu.playSoundException();
                JOptionPane.showMessageDialog(null, "You must select a department name.");
            }
        }
    }


    public void addAcademicTask(Course course) throws IOException, InvalidInputException {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField nameField = new JTextField();
        panel.add(new JLabel("Name: "));
        panel.add(nameField);
        JDatePickerImpl datePicker = createDatePicker();
        panel.add(new JLabel("Due Date: "));
        panel.add(datePicker);
        int result = JOptionPane.showConfirmDialog(null, panel, "Add Academic Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Item item = new AcademicTask(nameField.getText(), course, changeFormat(datePicker.getJFormattedTextField().getText()), false, false);
                checkAddSave(item);
            } catch (DuplicateItemException e) {
                MainMenu.playSoundException();
                JOptionPane.showMessageDialog(null, "Sorry, we already have an item with the name " + nameField.getText() + ". Please " +
                        "try a different name.");
                addAcademicTask();
            }
        }
    }

    public void addEvent() throws IOException {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField timeField = new JTextField();
        panel.add(new JLabel("Name: "));
        panel.add(nameField);
        panel.add(new JLabel("Location: "));
        panel.add(locationField);
        panel.add(new JLabel("Date (format MM d yyyy): "));
        JDatePickerImpl datePicker = createDatePicker();
        panel.add(datePicker);
        panel.add(new JLabel("Time (format HH:mm): "));
        panel.add(timeField);
        int result = JOptionPane.showConfirmDialog(null, panel, "Add Event",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Item item = new Event(nameField.getText(), locationField.getText(), changeFormat(datePicker.getJFormattedTextField().getText()), timeField.getText(), false, false);
                checkAddSave(item);
                areYouFinished();
            } catch (InvalidInputException | DuplicateItemException e) {
                if (e instanceof InvalidInputException) {
                    MainMenu.playSoundException();
                    JOptionPane.showMessageDialog(null, "Invalid input. Try Again.");
                } else {
                    MainMenu.playSoundException();
                    JOptionPane.showMessageDialog(null, "Sorry, we already have an item with the name " + nameField.getText()
                            + ". Please try again.");
                }
                addEvent();
            }
        }
    }

    public void checkAddSave(Item item) throws DuplicateItemException, IOException {
        checkItem(item);
        ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
        lois.addItem(item);
        item.save(FileManager.file);
    }

    public void checkItem(Item item) throws DuplicateItemException {
        ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
        if (lois.getMap().containsKey(item.getName())) {
            throw new DuplicateItemException(item.getName());
        }
    }

    public void areYouFinished() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Would you like to add more items?"));

        int result = JOptionPane.showConfirmDialog(null, panel, "Done?",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            done = false;
        } else if (result == JOptionPane.NO_OPTION) {
            done = true;
        }

    }

    public static JDatePickerImpl createDatePicker(){
        UtilDateModel model = new UtilDateModel();

        //the following code is inspired from a stackOverflow user. Link: https://stackoverflow.com/questions/26794698/how-do-i-implement-jdatepicker
        Properties p = new Properties();
        p.put("text.today", "Today");

        JDatePanelImpl panel = new JDatePanelImpl(model, p);
        DateComponentFormatter formatter = new DateComponentFormatter();

        JDatePickerImpl datePicker = new JDatePickerImpl(panel, formatter);

        return datePicker;
    }

    public static String changeFormat(String string){
        ArrayList<String> characters = new ArrayList<>(Arrays.asList(string.split("")));
        characters.remove(",");
        StringBuilder sb = new StringBuilder();
        for (String str: characters){
            sb.append(str);
        }
        return sb.toString();
    }
}




