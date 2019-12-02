package com.example.schedule;

import java.io.Serializable;


//get set方法由as自动生成code/generate
//构造方法 code/generate

public class Course implements Serializable {

    private String courseName,courseTeacher,courseAddress;
    private int courseDay,courseIndex,courseStartWeek,courseEndWeek,courseIsDouble;

    public Course(String courseName, String courseTeacher, String courseAddress, int courseDay, int courseIndex, int courseStartWeek, int courseEndWeek, int courseIsDouble) {
        this.courseName = courseName;
        this.courseTeacher = courseTeacher;
        this.courseAddress = courseAddress;
        this.courseDay = courseDay;
        this.courseIndex = courseIndex;
        this.courseStartWeek = courseStartWeek;
        this.courseEndWeek = courseEndWeek;
        this.courseIsDouble = courseIsDouble;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getCourseAddress() {
        return courseAddress;
    }

    public void setCourseAddress(String courseAddress) {
        this.courseAddress = courseAddress;
    }

    public int getCourseDay() {
        return courseDay;
    }

    public void setCourseDay(int courseDay) {
        this.courseDay = courseDay;
    }

    public int getCourseIndex() {
        return courseIndex;
    }

    public void setCourseIndex(int courseIndex) {
        this.courseIndex = courseIndex;
    }

    public int getCourseStartWeek() {
        return courseStartWeek;
    }

    public void setCourseStartWeek(int courseStartWeek) {
        this.courseStartWeek = courseStartWeek;
    }

    public int getCourseEndWeek() {
        return courseEndWeek;
    }

    public void setCourseEndWeek(int courseEndWeek) {
        this.courseEndWeek = courseEndWeek;
    }

    public int getCourseIsDouble() {
        return courseIsDouble;
    }

    public void setCourseIsDouble(int courseIsDouble) {
        this.courseIsDouble = courseIsDouble;
    }
}
