package Model;

import Model.Exceptions.InvalidInputException;

import java.util.ArrayList;

public class Course {


    private String department;
    private Integer courseCode;


    public Course(String department, String courseCode) throws InvalidInputException {
        if(department.length()==4) {
            this.department = department;
        }else {
            throw new InvalidInputException();
        }
        try {
            this.courseCode = Integer.parseInt(courseCode);
        }catch (NumberFormatException e){
            throw new InvalidInputException();
        }
    }

    public String printCourse(){
        return department + courseCode.toString();
    }

}
