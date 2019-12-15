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

    public CourseDaoImpl(SQLiteDatabase sqLiteDatabase) {
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
        //关闭资源
        sqLiteDatabase.close();
    }

    /*
    修改课程 需要修改 将前端的修改变成全部的信息，然后参数改为oldcourse和newcourse
     */
    @Override
    public void updateCourse(Course course, String newCourseName, String newTeacher, String newAddress) {

        sqLiteDatabase.execSQL(
                "update course set courser_name = ?, teacher = ?, classroom = ? " +
                        "where id = " + course.getId(),
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
        //清空表
        sqLiteDatabase.execSQL("delete from course");
        //还原id
        sqLiteDatabase.execSQL("update sqlite_sequence set seq=0 where name='course'");
        sqLiteDatabase.close();
    }

    @Override
    public List<Course> getAllCourses(int week) {
        //判断所选周是单周还是双周 为双周则查询0|2的课程 为单周则查询0|1的课程
        int isDouble = week % 2 == 0 ? 2 : 1;
        //查询结果集
        Cursor cursor = sqLiteDatabase.rawQuery("select * from course where " +
                "(class_start<=" + week + " and class_end>=" + week + ") and (isDouble=0 or isDouble=" + isDouble + ")", null);
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
