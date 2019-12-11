package com.example.schedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    //sql的内容应为创建表的sql语句,从左至右依次为 课程名称,教室名称,教室地点,周几上课(与课程card的col直接相关),第几节上课(与课程card的row直接相关),第几周开始,第几周结束,是否为单双周 *******后面几个字段为了判断方便,全部设为了int类型,目前的dialog方式全部为editText,待有余力后慢慢修改
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table course("+"id integer primary key autoincrement,"+"courser_name text,"+"teacher text,"+"classroom text,"+"day integer,"+"course_index integer,"+"class_start integer,"+"class_end integer,"+"isDouble integer)";
        db.execSQL(sql);
    }


    //抽象方法,必须被继承
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

