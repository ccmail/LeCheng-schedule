package com.example.schedule;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

/***
 *
 * 运行逻辑: 1.启动时先加载组件方法,并且查询数据库,查询数据库的时候会用到添加课程卡片的方法
 *          2.添加卡片的方法会从数据库中读取课程信息,进行循环添加
 *          3.点击添加的图片按钮时,弹出输入课程信息的dialog,点击dialog 的确定按钮时,获取输入的信息,并从course类中走一遍插入数据库中
 *          4.第3步插入数据库之后,立刻重新执行数据库的查询,并且插入卡片
 *          备注:添加卡片的方法在查询数据库的方法中被调用
 */


public class MainActivity extends AppCompatActivity {
    //定义的全局变量
    private Spinner week_spinner;
    private ArrayAdapter week_array;
    private GridLayout gridLayout;
    private ImageButton add_imageButton;
    private int width,height;
    private Toolbar toolbar;
    //创建数据库的方法
    private MyDataBaseHelper my_dataBase_helper = new MyDataBaseHelper(this,"database.db",null,1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setWeek_spinner(week_spinner,week_array);
        setGridLayout(gridLayout);
        setAdd_imageButton(add_imageButton);
        getWidth();
        show_sql();
//        add_content(1,1,"测试margin使用");
//        add_content(2,1,"测试使用得到,以后这里由course类获取用户的输入信息");
//        add_content(2,2,"测试使用得到,以后这里由course类获取用户的输入信息");
//        add_content(2,3,"测试使用得到,以后这里由course类获取用户的输入信息");

        }

//    加载toolbar的菜单按钮等
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    //控制各组件的方法

    //设置sinner的方法,绑定数组
    public void setWeek_spinner(Spinner week_spinner,ArrayAdapter week_array) {
        week_spinner = (Spinner)findViewById(R.id.week_count);
        week_array = ArrayAdapter.createFromResource(this,R.array.weeks,R.layout.support_simple_spinner_dropdown_item);
        week_spinner.setAdapter(week_array);

        this.week_spinner = week_spinner;
    }

    //设置图片按钮的方法,随着后期开发,该方法可能会被替换
    public void setAdd_imageButton(ImageButton add_imageButton) {
        add_imageButton = (ImageButton)findViewById(R.id.add_image);
        add_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this,"测试消息",Toast.LENGTH_SHORT);
                toast.show();
                setCourse_dialog();

            }
        });

        this.add_imageButton = add_imageButton;
    }

    //绑定gridLayout布局
    public void setGridLayout(GridLayout gridLayout) {
        gridLayout= (GridLayout)findViewById(R.id.schedule);
        this.gridLayout = gridLayout;
        //测试使用的提示信息
        Toast toast = Toast.makeText(MainActivity.this,"gridLayout绑定成功",Toast.LENGTH_SHORT);
        toast.show();
    }


    //添加卡片的方法,形参col,row是从用户那边获取到的数据,初步推测应与星期几和第几节课对应
    public void add_content(final Course course){
        int row = course.getCourseIndex();//row对应第几节课
        int col = course.getCourseDay();//col对应周几上课
        String text = course.getCourseName()+"\n"+course.getCourseAddress()+"\n"+course.getCourseTeacher();//将课程名称,地址,教师姓名显示出来,开始上课周数暂不显示

        //每节课的那一个总的卡片布局放在course_card.xml文件中,这里将其绑定到变量course_card_view上
        final View course_card_view= LayoutInflater.from(this).inflate(R.layout.course_card,null);


        //第一列节数列占比中较小,*1,其余均为*2
        int card_width = (this.width/15)*2-20;
        //绑定course_card.xml文件中的TextView
        TextView course_textView = course_card_view.findViewById(R.id.test_textview);
        //获取文本框的宽度再进行赋值,规避setWidth无效
        course_textView.getLayoutParams().width = card_width;
        course_textView.setPadding(5,5,5,5);
        course_textView.setText(text);
        //设置行号,这个方法设置权重需要更改minSdkVersion,详见README.MD
        GridLayout.Spec row_spec = GridLayout.spec(row,1,1.0f);
        //设置列号
        GridLayout.Spec col_spec = GridLayout.spec(col,1,1.0f);
        //将行列号合并
        GridLayout.LayoutParams col_row_spec = new GridLayout.LayoutParams(row_spec,col_spec);
        //将设置好的card按照设定好的行列添加到gridlayout中
        this.gridLayout.addView(course_card_view,col_row_spec);

        //为布置的卡片添加点击方法
        course_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_card(course,course_card_view);



            }
        });


    }

    //获取屏幕分辨率的方法,借此将卡片等宽放置,长度预先设定死的,所以暂且只需要设定宽度
    public void getWidth(){
        DisplayMetrics metrics = new DisplayMetrics();
        metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        //测试时的使用提示
        String a_width = String.valueOf(width);
        Toast toast = Toast.makeText(MainActivity.this,a_width,Toast.LENGTH_SHORT);
        toast.show();
        this.width=width;
        this.height=height;
    }

    //设置toolbar的方法,不会写,放弃了,预计将spinner(选择当前周)放在左边标题旁边,加号图标按钮放在最右边
    public void setToolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);




    }

    //创建添加课程的弹窗,确定添加后执行插入数据库与查询数据库的操作
    public void setCourse_dialog(){
        //创建弹窗用以获取用户输入的信息
        LayoutInflater course_dialog = LayoutInflater.from(MainActivity.this);
        final View v1 = course_dialog.inflate(R.layout.add_course_dialog,null);



        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("添加课程");
        builder.setView(v1);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //绑定输入框
                EditText courseName_edit = (EditText) v1.findViewById(R.id.course_name);
                EditText teacher_edit = (EditText) v1.findViewById(R.id.teacher_name);
                EditText address_edit = (EditText) v1.findViewById(R.id.course_address);
                EditText day_edit = (EditText)  v1.findViewById(R.id.course_day);
                EditText index_edit = (EditText) v1.findViewById(R.id.course_index);
                EditText start_edit = (EditText) v1.findViewById(R.id.course_start);
                EditText end_edit  = (EditText) v1.findViewById(R.id.course_end);
                EditText double_edit = (EditText) v1.findViewById(R.id.is_double);
                //将输入框的值转化为String,int类型
                String course_name  = courseName_edit.getText().toString();
                String teacher = teacher_edit.getText().toString();
                String address = address_edit.getText().toString();
                int day = Integer.parseInt(day_edit.getText().toString());
                int index = Integer.parseInt(index_edit.getText().toString());
                int start = Integer.parseInt(start_edit.getText().toString());
                int end = Integer.parseInt(end_edit.getText().toString());
                int is_double = Integer.parseInt(double_edit.getText().toString());
                //将数据从course构造函数中走一遍
                Course insert_course = new Course(course_name,teacher,address,day,index,start,end,is_double);
                //这里应该调用插入到数据库的方法,并且也应让其显示出来
                Insert_sql(insert_course);
                //应该有一个重新从数据库中检索数据的方法
                show_sql();
            }
        });
        //添加取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //显示dialog
        builder.create();
        builder.show();




    }

    //插入数据库的方法
    public void Insert_sql(Course course){
        Toast toast = Toast.makeText(MainActivity.this,"测试啊这是测试",Toast.LENGTH_SHORT);
        toast.show();
        //获取mydatabasehelper数据库连接类中的插入值或创建数据库的方法
        SQLiteDatabase sqLiteDatabase = my_dataBase_helper.getWritableDatabase();
        //插入字段的名字与对应的值
        sqLiteDatabase.execSQL("insert into course(courser_name,teacher,classroom,day,course_index,class_start,class_end,isDouble) values(?,?,?,?,?,?,?,?)",
                new String[]{course.getCourseName(),
                course.getCourseTeacher(),
                course.getCourseAddress(),
                course.getCourseDay()+"",
                course.getCourseIndex()+"",
                course.getCourseStartWeek()+"",
                course.getCourseEndWeek()+"",
                course.getCourseIsDouble()+""});
        //day等字段为int类型,加上空字符串,自动转化为String类型
        //从course中获取数据
    }

//    public void Update_sql(Course course){
//        SQLiteDatabase sqLiteDatabase = my_dataBase_helper.getWritableDatabase();
//        sqLiteDatabase.execSQL("update ");
//    }

    //从数据库查询的方法
    public void show_sql(){
        //将数据库中的数据存到数组
        ArrayList<Course> courses = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = my_dataBase_helper.getWritableDatabase();
        //移动数据库的游标
        Cursor cursor = sqLiteDatabase.rawQuery("select * from course",null);
        if (cursor.moveToFirst()){
            do {
                courses.add(new Course(
                        cursor.getString(cursor.getColumnIndex("courser_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("classroom")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("course_index")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end")),
                        cursor.getInt(cursor.getColumnIndex("isDouble"))
                ));
            }while (cursor.moveToNext());
            cursor.close();
        }

        //foreach,取出数组中的每一个元素
        for (Course course:courses){
            add_content(course);

        }


    }

    //添加左侧课程数目的方法,需修改
    public void lessonNum(int lesson_num){
        View num = LayoutInflater.from(this).inflate(R.layout.add_course_dialog,null);
        TextView lesson = num.findViewById(R.id.test_textview);

        int card_width = (this.width/15)*1-20;
        lesson.getLayoutParams().width = card_width;
        lesson.setPadding(5,5,5,5);
        for (int i = 1; i <=lesson_num; i++){
            String text = "第"+i+"节";
            lesson.setText(text);
        }

        GridLayout.Spec row_spec = GridLayout.spec(lesson_num,1,1.0f);
        GridLayout.Spec col_spec = GridLayout.spec(0,1,1.0f);

        GridLayout.LayoutParams spec = new GridLayout.LayoutParams(row_spec,col_spec);
        this.gridLayout.addView(num,spec);
    }

    //点击卡片调用的方法,用于弹框与删改
    public void click_card(final Course course, final View view_delete){
        Toast toast0 = Toast.makeText(MainActivity.this,"这里是点击卡片的测试",Toast.LENGTH_SHORT);
        toast0.show();

        LayoutInflater show_course = LayoutInflater.from(MainActivity.this);
        final View view = show_course.inflate(R.layout.show_card,null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("详细信息");
        builder.setView(view);
//        builder.create().show();
        //为了关闭AlertDialog,对其进行赋值,在下方进行调用
        final AlertDialog closeDialog = builder.create();
        closeDialog.show();


        //若需显示额外的东西,需要前往show_card.xml文件中进行添加组件
        //绑定show_card中的组件
        EditText name_edit = (EditText)view.findViewById(R.id.course_name);
        EditText teacher_edit = (EditText)view.findViewById(R.id.course_teacher);
        EditText address_edit = (EditText)view.findViewById(R.id.course_address);
        //获取相应的值显示在Dialog中
        name_edit.setText(course.getCourseName());
        teacher_edit.setText(course.getCourseTeacher());
        address_edit.setText(course.getCourseAddress());

        Button delete_button = (Button)view.findViewById(R.id.delete);
        Button submit_button = (Button)view.findViewById(R.id.submit);

        //删除课程的点击响应方法
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此处写删除的方法
                gridLayout.removeView(view_delete);
//                gridLayout.removeAllViews();
                SQLiteDatabase sqLiteDatabase = my_dataBase_helper.getWritableDatabase();
                sqLiteDatabase.execSQL("delete from course where courser_name = ?",new String[]{course.getCourseName()});
                Toast toast1 = Toast.makeText(MainActivity.this,"删除成功!",Toast.LENGTH_SHORT);
                toast1.show();
                //点击按钮后将dialog关闭
                closeDialog.dismiss();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里写插入的方法

                closeDialog.dismiss();
            }
        });
    }

    public void all_delete(){

    }

}
