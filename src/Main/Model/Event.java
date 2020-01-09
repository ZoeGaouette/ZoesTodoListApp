package Model;


import Model.Exceptions.InvalidInputException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Item {
    private String location;
    private LocalTime time;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");



    public Event(String name, String location, String due, String time, boolean complete, boolean leaveAsIs) throws InvalidInputException {
        super(name, due, complete, leaveAsIs);
        checkNull(location);
        checkNull(time);
        this.location = location;
        setTimeInitial(time);
        this.type = Type.EVENT;
    }

    //EFFECTS: prints line that will be saved into file
    @Override
    public String print(){
        String string0 = type.getType();
        String string1 = getName();
        String string2 = getLocation();
        String string3 = getDue().format(format);
        String string4 = getTime().toString();
        String string5 = String.valueOf(getComplete());
        String string6 = String.valueOf(getLeaveAsIs());
        return string0 + "," + string1 + "," + string2 + "," + string3 + "," + string4 + "," + string5 + "," + string6;
    }


    @Override
    public String printItem() {
        return "Event: " + getName() +
                "\n" + "Location: " + getLocation() + "\n" + "Date: " + getDue() + "\n" + "Time: " + getTime() + "\n" + printItemCompletionStatus() + printItemOverdueStatus();
    }


    public void setLocation(String location) throws InvalidInputException {
        checkNull(location);
        this.location = location;
        notifyObservers();
    }

    public void setTimeInitial(String time) throws InvalidInputException {
        try{
            this.time = LocalTime.parse(time, timeFormatter);
        }catch (DateTimeParseException e){
            throw new InvalidInputException();
        }

    }

    public void setTime(String time) throws InvalidInputException {
        setTimeInitial(time);
        notifyObservers();
    }


    public LocalTime getTime(){
        return time;
    }

    public String getLocation() { return location;}



}
