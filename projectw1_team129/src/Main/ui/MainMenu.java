package ui;


import Model.Exceptions.EmptyListException;
import Model.Exceptions.FileNotLoadedException;
import Model.ListOfItemSingleton;
import Observer.Observer;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenu extends JFrame implements ActionListener, Observer {

    private JPanel content, buttons;
    private static JTextArea currentList;
    private JScrollPane scroll;
    private JButton createList, addItems, editItems, loadAFile, exit;
    private JTextArea title;

    
    public MainMenu(){
        initializeFields();
        setTitle("Zoe's To-do List Application");
        setSize(1500, 1500);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void initializeFields(){
        content = new JPanel();
        content.setSize(1000, 1000);
        content.setLayout(new GridBagLayout());
        content.setBorder(BorderFactory.createBevelBorder(5,Color.WHITE, Color.WHITE));

        buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        buttons.setBackground(Color.WHITE);

        currentList = new JTextArea();
        currentList.setMargin(new Insets(10,30,10,10));
        DefaultCaret caret = (DefaultCaret)currentList.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        scroll = new JScrollPane(currentList);
        scroll.setPreferredSize(new Dimension(1000, 1000));
        scroll.setWheelScrollingEnabled(true);
        scroll.getVerticalScrollBar().setValue(0);
        scroll.setBackground(Color.WHITE);
        currentList.setEditable(false);
        currentList.setSize(2000, 2000);
        Font font = new Font("TimesRoman", Font.PLAIN, 50);
        currentList.setFont(font);
        content.add(scroll);
        createButtons();
    }

    public void createAndShowGUI(){
        setCurrentList();
        JPanel eastArea = new JPanel(new GridBagLayout());
        eastArea.setBackground(Color.WHITE);
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.gridy = 1;
        gbConstraints.gridx = 1;
        title = new JTextArea(changeTitle());
        title.setFont(new Font("TimesRoman", Font.BOLD, 50));
        eastArea.add(title, gbConstraints);
        gbConstraints.gridy =2;
        eastArea.add(content, gbConstraints);
        this.add(eastArea, BorderLayout.EAST);
        this.add(buttons, BorderLayout.WEST);
        setClosing();
        this.pack();
        this.setVisible(true);
    }


    public void createButtons(){
        Font font = new Font("Ariel", Font.BOLD,24);
        Dimension dimension = new Dimension(300, 200);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        createList = new JButton("Create A To-do List");
        setButtonProperties(createList,dimension,font, Color.LIGHT_GRAY);
        buttons.add(createList, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;

        addItems = new JButton("Add items");
        setButtonProperties(addItems, dimension, font, Color.WHITE);
        buttons.add(addItems, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;

        editItems = new JButton("Edit Items");
        setButtonProperties(editItems, dimension, font, Color.LIGHT_GRAY);
        buttons.add(editItems, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;

        loadAFile = new JButton("Load A To-do List");
        setButtonProperties(loadAFile, dimension, font, Color.WHITE);
        buttons.add(loadAFile,constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;

        exit = new JButton("Save and Exit");
        setButtonProperties(exit, dimension, font, Color.LIGHT_GRAY);
        buttons.add(exit, constraints);

        setListeners();

    }

    public void setButtonProperties(JButton button, Dimension dimension, Font font, Color color){
        button.setPreferredSize(dimension);
        button.setBackground(color);
        button.setFont(font);
    }


    public void setListeners(){
        createList.addActionListener(this);
        addItems.addActionListener(this);
        editItems.addActionListener(this);
        loadAFile.addActionListener(this);
        exit.addActionListener(this);
    }


    //This is partially inspired by a stack overflow user. I modified the code to suit my needs
    //Link: https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event

    public void setClosing(){
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
                try {
                    lois.save(FileManager.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

    public void setCurrentList() {
        if (!(currentList == null)) {
            currentList.setText(ListOfItemSingleton.getInstance().printList());
            currentList.setFont(new Font("TimesRoman", Font.PLAIN, 44 ));
        }
    }

    public void setTitle() {
        if (!(title == null)) {
            title.setText(changeTitle());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ListManager lm = new ListManager();
        if (e.getSource().equals(createList)){
            try {
                lm.listCreator();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else if (e.getSource().equals(addItems)){
            try {
                TaskManager tm = new TaskManager();
                tm.itemAdder();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else if (e.getSource().equals(editItems)){
            TaskEditorManager tem = new TaskEditorManager();
            try {
                tem.itemSearcher();
            } catch (EmptyListException e1) {
                infoBox("Your list is empty!", "Empty list");
            }
        } else if (e.getSource().equals(loadAFile)){
            String initialFileName = FileManager.file.getName();
            FileManager fm = new FileManager();
            try {
                fm.fileChooser(fm.fileLoader(), initialFileName);
            } catch (IOException e1) {
                infoBox("File couldn't be loaded", "File Not Found");
            } catch (FileNotLoadedException e1) {
                infoBox("File couldn't be loaded", "File not loaded");
            }
        }
        else if (e.getSource().equals(exit)){
            ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
            try {
                lois.save(FileManager.file);
                System.exit(0);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void update() {
        setCurrentList();
    }

    public static String changeTitle() {
        String name = FileManager.file.getName();
        ArrayList<String> characters = new ArrayList<>(Arrays.asList(name.split("")));

        while (characters.contains("_")) {
            {
                int index = characters.indexOf("_");
                characters.set(index," ");
            }
        }
        StringBuilder sb = new StringBuilder();
        for(String string: characters){
            sb.append(string);
        }

        String s = sb.toString();
        s = s.substring(0,s.length()-4);
        return s;
    }


    //This code is borrowed from stackOverflow. I modified it a bit to fit my needs.
    //Link: https://stackoverflow.com/questions/3927941/system-sounds-in-java

    public static void playSoundException(){
        Runnable exceptionSound =
                (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
        exceptionSound.run();
    }
}
