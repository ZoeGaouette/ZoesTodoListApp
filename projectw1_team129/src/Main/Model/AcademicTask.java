package Model;


import Model.Exceptions.InvalidInputException;

import java.time.format.DateTimeFormatter;

public class AcademicTask extends Item {

    private Course course;

    public AcademicTask(String name, Course course, String due, boolean complete, boolean leaveAsIs) throws InvalidInputException {
        super(name, due, complete, leaveAsIs);
        checkNull(course);
        this.course = course;
        this.type = Type.ACADEMICTASK;
    }



    @Override
    public String printItem(){
        return printItemName() + "Course: " + course.printCourse() + "\n" + printItemDue() +
                printItemCompletionStatus() + printItemOverdueStatus();
    }

    @Override
    public String print(){
        String string0 = type.getType();
        String string1 = getName();
        String string2 = course.printCourse();
        String string3 = getDue().format(format);
        String string4 = String.valueOf(getComplete());
        String string5 = String.valueOf(getLeaveAsIs());
        return string0 + "," + string1 + "," + string2 + "," + string3 + "," + string4 + "," + string5;
    }


    public void setCourse(Course course){
        this.course = course;
        notifyObservers();
    }
}
