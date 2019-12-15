package com.example.schedule.service;

import com.example.schedule.entity.Course;

import java.util.List;

public interface CourseService {

    public void insertCourse(Course course);

    public void updateCourse(Course course,String newCourseName,String newTeacher, String newAddress);

    public void removeCourse(Course course);

    public void removeAllCourses();

    public List<Course> getAllCourses(int week);

}
