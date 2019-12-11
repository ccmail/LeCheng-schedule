package com.example.schedule.dao;

import com.example.schedule.entity.Course;

import java.util.List;

public interface CourseDao {

    public void insertCourse(Course course);

    public void updateCourse(Course course,String newCourseName,String newTeacher, String newAddress);

    public void removeCourse(Course course);

    public void removeAllCourses();

    public List<Course> getAllCourses();
}
