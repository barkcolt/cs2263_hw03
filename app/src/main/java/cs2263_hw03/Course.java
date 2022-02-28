package cs2263_hw03;

import java.io.*;
import com.google.gson.Gson;

public class Course implements java.io.Serializable {
    int creditVal;
    int courseNum;
    String name;
    String deparment;
    static String[] departmentCodes = {"CS","MATH","CHEM","PHYS","BIOL","EE"};
    public Course() {

    }
    public Course(int num, String name, int credit, String dept) {
        this.creditVal = credit;
        this.courseNum = num;
        this.name = name;
        this.deparment = dept;
    }

    public String toString() {
        return "Course Number: " + courseNum + "\nCourse Name: " + name + "\nCredits available: " + creditVal + "\nDepartment: " + deparment;
    }

    public boolean saveJSON() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        try {
            FileWriter file = new FileWriter("courses.json", true);
            file.write(json);
            file.close();
            return true;
        } catch (IOException i) {
            return false;
        }
    }
}
