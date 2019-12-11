package com.example.schedule.daoImpl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.schedule.dao.CourseDao;
import com.example.schedule.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements CourseDao {
    //数据库连接
    SQLiteDatabase sqLiteDatabase;
    //实例化时设置数据库连接
    public CourseDaoImpl(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @Override
    public void insertCourse(Course course) {
        sqLiteDatabase.execSQL(
                "insert into course(courser_name,teacher,classroom,day,course_index,class_start,class_end,isDouble) values(?,?,?,?,?,?,?,?)",
                new Object[]{
                        course.getCourseName(),
                        course.getCourseTeacher(),
                        course.getCourseAddress(),
                        course.getCourseDay(),
                        course.getCourseIndex(),
                        course.getCourseStartWeek(),
                        course.getCourseEndWeek(),
                        course.getCourseIsDouble()
                }
        );
        sqLiteDatabase.close();
    }

    @Override
    public void updateCourse(Course course,String newCourseName,String newTeacher, String newAddress) {

        sqLiteDatabase.execSQL(
                "update course set courser_name = ?, teacher = ?, classroom = ? " +
                        "where id = "+course.getId(),
                new Object[]{newCourseName, newTeacher, newAddress}
        );
        sqLiteDatabase.close();
    }

    @Override
    public void removeCourse(Course course) {
        sqLiteDatabase.execSQL(
                "delete from course where id = ?",
                new Object[]{course.getId()}
        );
        sqLiteDatabase.close();
    }

    @Override
    public void removeAllCourses() {
        sqLiteDatabase.execSQL("delete from course");
        sqLiteDatabase.close();
    }

    @Override
    public List<Course> getAllCourses() {

        Cursor cursor = sqLiteDatabase.rawQuery("select * from course",null);
        Course tmpCourse;
        List<Course> courses = new ArrayList<>();
        while (cursor.moveToNext()) {
            tmpCourse = new Course();
            tmpCourse.setId(cursor.getInt(cursor.getColumnIndex("id")));
            tmpCourse.setCourseName(cursor.getString(cursor.getColumnIndex("courser_name")));
            tmpCourse.setCourseTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
            tmpCourse.setCourseAddress(cursor.getString(cursor.getColumnIndex("classroom")));
            tmpCourse.setCourseDay(cursor.getInt(cursor.getColumnIndex("day")));
            tmpCourse.setCourseIndex(cursor.getInt(cursor.getColumnIndex("course_index")));
            tmpCourse.setCourseStartWeek(cursor.getInt(cursor.getColumnIndex("class_start")));
            tmpCourse.setCourseEndWeek(cursor.getInt(cursor.getColumnIndex("class_end")));
            tmpCourse.setCourseIsDouble(cursor.getInt(cursor.getColumnIndex("isDouble")));
            courses.add(tmpCourse);
        }
        cursor.close();
        sqLiteDatabase.close();
        return courses;
    }

}
