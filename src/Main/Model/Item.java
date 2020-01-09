package Model;




import Model.Exceptions.InvalidInputException;
import Observer.Observer;
import Observer.Subject;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.io.*;
import java.time.format.DateTimeParseException;
import java.util.Objects;


public abstract class Item extends Subject implements Saveable, Printing {

    protected Type type;
    protected String name;
    protected LocalDate due;
    protected boolean complete;
    protected boolean overdue;
    protected LocalDate today = LocalDate.now();
    protected boolean leaveAsIs;

    protected DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy");


    //MODIFIES: this
    //EFFECTS: constructs Item, with name, Due Date, and whether its complete/overdue
    public Item(String name, String due, boolean complete, boolean leaveAsIs) throws InvalidInputException {
        checkNull(name);
        checkNull(due);
        this.name = name;
        setDueInitial(due);
        this.complete = complete;
        this.leaveAsIs = leaveAsIs;
        Observer observer = ListOfItemSingleton.getInstance();
        addObserver(observer);
    }

    //EFFECTS: returns name of item
    public String getName() {
        return name;
    }

    //EFFECTS: returns due date of item
    public LocalDate getDue() {
        return due;
    }

    //EFFECTS: returns completion status of item
    public Boolean getComplete() {
        return complete;
    }

    //EFFECTS: returns completion status of item in Complete/Incomplete string format
    public String getCompletePrint() {
        if (complete) {
            return "Complete";
        } else {
            return "Incomplete";
        }
    }


    //EFFECTS: returns true if item is overdue, false otherwise
    public boolean getOverdue() {
        return overdue;
    }

    //EFFECTS: returns overdue status of item in Y/N string format
    public String getOverduePrint() {
        if (overdue) {
            return "yes";
        } else {
            return "no";
        }
    }

    //EFFECTS: returns type of task
    public Type getType(){
        return type;
    }

    //EFFECTS: returns whether user has indicated to leave item as is
    public boolean getLeaveAsIs(){
        return leaveAsIs;
    }

    //MODIFIES: this
    //EFFECTS: assigns name to item
    public void setName(String name) throws InvalidInputException {
        checkNull(name);
        this.name = name;
        ListOfItemSingleton lois = ListOfItemSingleton.getInstance();
        lois.removeItem(this);
        lois.addItem(this);
        notifyObservers();
    }


    public void setDue(String due) throws InvalidInputException {
        setDueInitial(due);
        notifyObservers();
    }

    //REQUIRES: due must be in (MMM d, yyyy) format, or be "today"/"Today" or "tomorrow"/"Tomorrow"
    //MODIFIES: this
    //EFFECTS: assigns due date to item
    public void setDueInitial(String due) throws InvalidInputException {
        if (due.equals("tomorrow") || (due.equals("Tomorrow"))) {
            this.due = today.plusDays(1);
            setOverdue(today.plusDays(1));
        } else if (due.equals("today") || due.equals("Today")) {
            this.due = today;
            setOverdue(today);
        } else {
            try {
                LocalDate dueDate = LocalDate.parse(due, format);
                this.due = dueDate;
                setOverdue(dueDate);
            } catch (DateTimeParseException e) {
                throw new InvalidInputException();
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: assigns completion status to item
    public void setComplete(Boolean complete) {
        this.complete = complete;
        notifyObservers();
    }

    //REQUIRES: date is in LocalDate format
    //MODIFIES: this
    //EFFECTS: sets overdue status to item
    public void setOverdue(LocalDate date) {
        this.overdue = date.isBefore(today);
    }

    public void setLeaveAsIs(boolean bool){
        this.leaveAsIs = bool;
    }

    //EFFECTS: produces true if due date is overdue
    public boolean isOverdue() {
        return due.isBefore(today);
    }

    //EFFECTS: prints item
    abstract public String printItem();


    public String printItemName(){
        return "Task: " + getName() + "\n";
    }

    public String printItemDue(){
        return "Due Date: " + getDue() + "\n";
    }

    public String printItemCompletionStatus(){
        return "Completion Status: " + getCompletePrint() + "\n";
    }

    public String printItemOverdueStatus(){
        return "Overdue? " + getOverduePrint();
    }

    //EFFECTS: returns line that will be printed in file
    @Override
    abstract public String print();


    //EFFECTS: saves item to file
    public void save(File file) throws IOException{
        PrintWriter writer = new PrintWriter(new FileOutputStream(file, true));

        if (file.length() <= 1) {
            writer.print(print());
        }
        else {
            writer.print("\n" + print());
        }
        writer.close();
    }

    public void checkNull(Object o) throws InvalidInputException {
        if (o == null){
            throw new InvalidInputException();
        }
        else if ((o instanceof String)&&(o.equals(""))){
            throw new InvalidInputException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}






