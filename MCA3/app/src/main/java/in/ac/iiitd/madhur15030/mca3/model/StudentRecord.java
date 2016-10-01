package in.ac.iiitd.madhur15030.mca3.model;

import java.io.Serializable;

/**
 * Created by Madhur on 30/09/16.
 */
public class StudentRecord implements Serializable {
    public String rollno;
    public String name;
    public String currsem;

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrsem() {
        return currsem;
    }

    public void setCurrsem(String currsem) {
        this.currsem = currsem;
    }

    public StudentRecord(String rollno, String name, String currsem) {
        this.rollno = rollno;
        this.name = name;
        this.currsem = currsem;
    }
}
