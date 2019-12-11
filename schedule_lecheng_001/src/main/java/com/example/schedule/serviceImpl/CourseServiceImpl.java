package com.example.schedule.serviceImpl;

import android.database.sqlite.SQLiteDatabase;

import com.example.schedule.dao.CourseDao;
import com.example.schedule.daoImpl.CourseDaoImpl;
import com.example.schedule.entity.Course;
import com.example.schedule.service.CourseService;

import java.util.List;

public class CourseServiceImpl implements CourseService {

    SQLiteDatabase sqLiteDatabase;

    public CourseServiceImpl(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    CourseDao courseDao = new CourseDaoImpl(sqLiteDatabase);

    @Override
    public void insertCourse(Course course) {
        courseDao.insertCourse(course);
    }

    @Override
    public void updateCourse(Course course,String newCourseName,String newTeacher, String newAddress) {
        courseDao.updateCourse(course,newCourseName,newTeacher,newAddress);
    }

    @Override
    public void removeCourse(Course course) {
        courseDao.removeCourse(course);
    }

    @Override
    public void removeAllCourses() {
        courseDao.removeAllCourses();
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }
}
