package ui;

import Model.Exceptions.FileNotLoadedException;
import Model.Exceptions.InvalidInputException;
import Model.ListOfItemSingleton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    public static File file;

    public static void setFile(File newFile){
         file= newFile;
    }

    public ArrayList fileLoader() {
       // File folder = new File("/Users/zoega/IdeaProjects/projectw1_team129");
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        ArrayList<File> files = new ArrayList<>();
        for (File file : listOfFiles) {
            if (!(file.getName().equals("loadableTest.txt") || (file.getName().equals("saveableTest.txt")) || (file.getName().equals("bad.txt")))) {
                if (file.getName().endsWith(".txt")) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    public void fileChooser(ArrayList<File> list, String initial) throws IOException, FileNotLoadedException {

        ArrayList<String> fileNames = new ArrayList<>();
        for (File file : list) {
            fileNames.add(file.getName());
        }
        Object[] items = fileNames.toArray();
        JComboBox<Object> options = new JComboBox<>(items);
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Here are the to-do lists we have stored, which one would you like to load?"));
        panel.add(options);
        int result = JOptionPane.showConfirmDialog(null, panel, "Load a to-do list",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION){
            for (File file: list){
                if (file.getName().equals(options.getSelectedItem().toString())){
                    fileFound(file);
                }
            }
        }
//
//        for (File file : list) {
//            if (file.getName().equals(name)) {
//                fileFound(file);
//            } else if (isInteger(name)) {
//                if (list.indexOf(file) == Integer.parseInt(name)) {
//                    fileFound(file);
//                }
//            }
//        }
//        if (initial.equals(file.getName())) {
//            if (isInteger(name)) {
//                if (!(Integer.parseInt(name) == list.indexOf(file))) {
//                    throw new java.io.FileNotFoundException();
//                }
//            } else if (!(name.equals(file.getName()))) {
//                    throw new java.io.FileNotFoundException();
//            }
//        }
    }


    public void fileFound(File newFile) throws IOException {
        ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
        lois.save(file);
        lois.clearMap();
        file = newFile;
        try {
            lois.load(file);
        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(null, "We couldn't find that file.");
        }
        JOptionPane.showMessageDialog(null,"I have loaded " + newFile.getName() + " for you!");
    }


    public void createNewFile(String fileName) throws IOException {
        ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
        lois.save(file);
        lois.clearMap();
        File newFile = new File(fileName + ".txt");
        setFile(newFile);
    }


    public boolean isInteger(String s){
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
}



