package com.hddata.technologystudy.bean;

import java.util.List;

/**
 * Created by Orange on 2017/4/13.
 */

public class Student {
    private String name;
    private List<String> course;

    public Student(String name, List<String> course) {
        this.name = name;
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCourse() {
        return course;
    }

    public void setCourse(List<String> course) {
        this.course = course;
    }
}
