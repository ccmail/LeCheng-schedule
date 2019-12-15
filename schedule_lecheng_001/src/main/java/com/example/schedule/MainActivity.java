package com.example.schedule;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedule.entity.Course;
import com.example.schedule.service.CourseService;
import com.example.schedule.serviceImpl.CourseServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 运行逻辑: 1.启动时先加载组件方法,并且查询数据库,查询数据库的时候会用到添加课程卡片的方法
 * 2.添加卡片的方法会从数据库中读取课程信息,进行循环添加
 * 3.点击添加的图片按钮时,弹出输入课程信息的dialog,点击dialog 的确定按钮时,获取输入的信息,并从course类中走一遍插入数据库中
 * 4.第3步插入数据库之后,立刻重新执行数据库的查询,并且插入卡片
 * 备注:添加卡片的方法在查询数据库的方法中被调用
 */


public class MainActivity extends AppCompatActivity {
    //定义的全局变量
    private Spinner week_spinner;
    private ArrayAdapter week_array;
    private GridLayout gridLayout;
    private ImageButton add_imageButton;
    private int width, height;
    private Toolbar toolbar;
    //创建数据库的方法
    private MyDataBaseHelper my_dataBase_helper = new MyDataBaseHelper(this, "database.db", null, 1);
    SQLiteDatabase sqLiteDatabase;
    CourseService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initToolbar();
        setWeek_spinner(week_spinner, week_array);
        setGridLayout(gridLayout);
        getWidth();
        show_sql();

    }

    private void initToolbar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar.inflateMenu(R.menu.add_menu);
        this.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int menuItemId = menuItem.getItemId();
                System.out.println();
                switch (menuItemId) {
                    case R.id.addCourse:
                        setCourse_dialog();
                        break;
                    case R.id.removeAllCourse:
                        removeAllCourses();
                        break;
                }
                return true;
            }
        });
    }


    public void removeAllCourseCards() {
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 9; j++) {

            }
        }
    }
    public void removeAllCourses(){

        sqLiteDatabase = my_dataBase_helper.getWritableDatabase();
        service = new CourseServiceImpl(sqLiteDatabase);
        service.removeAllCourses();
        show_sql();
        //提示信息
        Toast toast = Toast.makeText(MainActivity.this, "删除全部课程", Toast.LENGTH_SHORT);
        toast.show();

    }

    //控制各组件的方法

    //设置spinner的方法,绑定数组
    public void setWeek_spinner(Spinner week_spinner, ArrayAdapter week_array) {
        week_spinner = (Spinner) findViewById(R.id.week_count);
        week_array = ArrayAdapter.createFromResource(this, R.array.weeks, R.layout.support_simple_spinner_dropdown_item);
        week_spinner.setAdapter(week_array);

        this.week_spinner = week_spinner;
    }


    //绑定gridLayout布局
    public void setGridLayout(GridLayout gridLayout) {
        gridLayout = (GridLayout) findViewById(R.id.schedule);
        this.gridLayout = gridLayout;
        //测试使用的提示信息
        Toast toast = Toast.makeText(MainActivity.this, "gridLayout绑定成功", Toast.LENGTH_SHORT);
        toast.show();
    }


    //添加卡片的方法,形参col,row是从用户那边获取到的数据,初步推测应与星期几和第几节课对应
    public void add_content(final Course course) {
        int row = course.getCourseIndex();//row对应第几节课
        int col = course.getCourseDay();//col对应周几上课
        String text = course.getCourseName() + "\n" + course.getCourseAddress() + "\n" + course.getCourseTeacher();//将课程名称,地址,教师姓名显示出来,开始上课周数暂不显示

        //每节课的那一个总的卡片布局放在course_card.xml文件中,这里将其绑定到变量course_card_view上
        final View course_card_view = LayoutInflater.from(this).inflate(R.layout.course_card, null);

        //第一列节数列占比中较小,*1,其余均为*2
        int card_width = (this.width / 15) * 2 - 20;
        //绑定course_card.xml文件中的TextView
        TextView course_textView = course_card_view.findViewById(R.id.test_textview);
        //获取文本框的宽度再进行赋值,规避setWidth无效
        course_textView.getLayoutParams().width = card_width;
        course_textView.setPadding(5, 5, 5, 5);
        course_textView.setText(text);
        //设置行号,这个方法设置权重需要更改minSdkVersion,详见README.MD
        GridLayout.Spec row_spec = GridLayout.spec(row, 1, 1.0f);
        //设置列号
        GridLayout.Spec col_spec = GridLayout.spec(col, 1, 1.0f);
        //将行列号合并
        GridLayout.LayoutParams col_row_spec = new GridLayout.LayoutParams(row_spec, col_spec);
        //将设置好的card按照设定好的行列添加到gridlayout中
        this.gridLayout.addView(course_card_view, col_row_spec);

        //为布置的卡片添加点击方法
        course_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_card(course, course_card_view);
            }
        });


    }

    //获取屏幕分辨率的方法,借此将卡片等宽放置,长度预先设定死的,所以暂且只需要设定宽度
    public void getWidth() {
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        //测试时的使用提示
        String a_width = String.valueOf(width);
        Toast toast = Toast.makeText(MainActivity.this, a_width, Toast.LENGTH_SHORT);
        toast.show();
        this.width = width;
        this.height = height;
    }

    //设置toolbar的方法,不会写,放弃了,预计将spinner(选择当前周)放在左边标题旁边,加号图标按钮放在最右边
    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }




    //创建添加课程的弹窗,确定添加后执行插入数据库与查询数据库的操作
    public void setCourse_dialog() {
        //创建弹窗用以获取用户输入的信息
        LayoutInflater course_dialog = LayoutInflater.from(MainActivity.this);
        final View v1 = course_dialog.inflate(R.layout.add_course_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("添加课程");
        builder.setView(v1);
        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //绑定输入框
                EditText courseName_edit = (EditText) v1.findViewById(R.id.course_name);
                EditText teacher_edit = (EditText) v1.findViewById(R.id.teacher_name);
                EditText address_edit = (EditText) v1.findViewById(R.id.course_address);
                EditText day_edit = (EditText) v1.findViewById(R.id.course_day);
                EditText index_edit = (EditText) v1.findViewById(R.id.course_index);
                EditText start_edit = (EditText) v1.findViewById(R.id.course_start);
                EditText end_edit = (EditText) v1.findViewById(R.id.course_end);
                EditText double_edit = (EditText) v1.findViewById(R.id.is_double);
                //将输入框的值转化为String,int类型
                String course_name = courseName_edit.getText().toString();
                String teacher = teacher_edit.getText().toString();
                String address = address_edit.getText().toString();
                int day = Integer.parseInt(day_edit.getText().toString());
                int index = Integer.parseInt(index_edit.getText().toString());
                int start = Integer.parseInt(start_edit.getText().toString());
                int end = Integer.parseInt(end_edit.getText().toString());
                int is_double = Integer.parseInt(double_edit.getText().toString());
                //将数据从course构造函数中走一遍
                Course insert_course = new Course();
                insert_course.setCourseName(course_name);
                insert_course.setCourseTeacher(teacher);
                insert_course.setCourseAddress(address);
                insert_course.setCourseDay(day);
                insert_course.setCourseIndex(index);
                insert_course.setCourseStartWeek(start);
                insert_course.setCourseEndWeek(end);
                insert_course.setCourseIsDouble(is_double);
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

    //添加课程
    public void Insert_sql(Course course) {
        //获取mydatabasehelper数据库连接类中的插入值或创建数据库的方法
        sqLiteDatabase = my_dataBase_helper.getWritableDatabase();
        //调用service接口并插入课程
        service = new CourseServiceImpl(sqLiteDatabase);
        service.insertCourse(course);
    }


    //添加左侧课程数目的方法,需修改
    public void lessonNum(int lesson_num) {
        View num = LayoutInflater.from(this).inflate(R.layout.add_course_dialog, null);
        TextView lesson = num.findViewById(R.id.test_textview);

        int card_width = (this.width / 15) * 1 - 20;
        lesson.getLayoutParams().width = card_width;
        lesson.setPadding(5, 5, 5, 5);
        for (int i = 1; i <= lesson_num; i++) {
            String text = "第" + i + "节";
            lesson.setText(text);
        }

        GridLayout.Spec row_spec = GridLayout.spec(lesson_num, 1, 1.0f);
        GridLayout.Spec col_spec = GridLayout.spec(0, 1, 1.0f);

        GridLayout.LayoutParams spec = new GridLayout.LayoutParams(row_spec, col_spec);
        this.gridLayout.addView(num, spec);
    }

    //从数据库查询的方法
    public void show_sql() {
        //获取数据库连接并调用service接口
        sqLiteDatabase = my_dataBase_helper.getWritableDatabase();
        service = new CourseServiceImpl(sqLiteDatabase);
        //获取course列表
        List<Course> courses= service.getAllCourses();
        //foreach,取出数组中的每一个元素
        for (Course course : courses) {
            //在activity上进行添加课程
            add_content(course);
        }
    }

    //点击卡片调用的方法,用于弹框与删改
    public void click_card(final Course course, final View view_delete) {
        LayoutInflater show_course = LayoutInflater.from(MainActivity.this);
        final View view = show_course.inflate(R.layout.show_card, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("课程信息");
        builder.setView(view);

        //为了关闭AlertDialog,对其进行赋值,在下方进行调用
        final AlertDialog closeDialog = builder.create();
        closeDialog.show();

        //若需显示额外的东西,需要前往show_card.xml文件中进行添加组件
        //绑定show_card中的组件
        final EditText name_edit = (EditText) view.findViewById(R.id.course_name);
        final EditText teacher_edit = (EditText) view.findViewById(R.id.course_teacher);
        final EditText address_edit = (EditText) view.findViewById(R.id.course_address);
        //获取相应的值显示在Dialog中
        name_edit.setText(course.getCourseName());
        teacher_edit.setText(course.getCourseTeacher());
        address_edit.setText(course.getCourseAddress());

        Button delete_button = (Button) view.findViewById(R.id.delete);
        Button submit_button = (Button) view.findViewById(R.id.submit);

        //删除课程的点击响应方法
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除表格布局上的view
                gridLayout.removeView(view_delete);
                //删除数据库中的课程
                sqLiteDatabase = my_dataBase_helper.getWritableDatabase();
                service = new CourseServiceImpl(sqLiteDatabase);
                service.removeCourse(course);

                Toast toast = Toast.makeText(MainActivity.this, "删除成功!", Toast.LENGTH_SHORT);
                toast.show();
                //点击按钮后将dialog关闭
                closeDialog.dismiss();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里写插入的方法
                String newCourseName = name_edit.getText().toString();
                String newTeacher = teacher_edit.getText().toString();
                String newAddress = address_edit.getText().toString();
                gridLayout.removeView(view_delete);

                sqLiteDatabase = my_dataBase_helper.getWritableDatabase();
                service = new CourseServiceImpl(sqLiteDatabase);
                service.updateCourse(course,newCourseName,newTeacher,newAddress);
                //这部分暂时这样写
                course.setCourseName(newCourseName);
                course.setCourseTeacher(newTeacher);
                course.setCourseAddress(newAddress);
                add_content(course);
                //提示信息
                Toast toast = Toast.makeText(MainActivity.this, "修改成功!", Toast.LENGTH_SHORT);
                toast.show();
                closeDialog.dismiss();
            }
        });

    }

    //用于添加单个的空白textview的方法,用于覆盖原先的表格卡片
    public void overWrite_empty(int x,int y){
        /***
         * 方法思路:
         * 1.先创建一个想要添加的组件的layout的xml文件
         * 2.获取那个文件的(View view变量,inflate方法)
         * 3.控制大小这一块是我没有理解的一块,没弄明白layout的xml里match和wrap宽高是怎么判定的,所以宽高这部分我暂时时使用人为设置定死的宽高的方式来做的
         * 4.获取x,y坐标变量(gridLayout.LayoutParams将x,y变量封装到一起)
         * 5.添加到gridlayout中(其他布局也可以使用addview,不过具体的位置需要查看他们已经写好的方法)
         */
        //获取到创建的空白textview组件(overwrite_empty.xml中)
        View view = LayoutInflater.from(this).inflate(R.layout.overwrite_empty,null);

        //一下关于设置textview长宽的内容均为推测,暂无法确定match是否能仅填满单个表格块的大小,若有更详细的认知,可将以下几行代码注释掉
        int card_width = (this.width / 15) * 2 - 20;
        TextView empty_text = (TextView)view.findViewById(R.id.empty);
            //为其设置宽度,权宜之计
        empty_text.getLayoutParams().width = card_width;
        //设置宽度大小的方法结束

        //设置具体的坐标位置,(添加到gridLayout上的坐标位置)
        GridLayout.Spec x_spec = GridLayout.spec(x, 1, 1.0f);
        GridLayout.Spec y_spec = GridLayout.spec(y, 1, 1.0f);
        GridLayout.LayoutParams xy = new GridLayout.LayoutParams(x_spec,y_spec);
        //添加道gridLayout中,view为show_card整个页面,xy为具体的坐标
        this.gridLayout.addView(view,xy);
    }

}
