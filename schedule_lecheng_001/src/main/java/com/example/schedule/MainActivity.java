package com.example.schedule;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
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
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    //定义的全局变量
    private Spinner week_spinner;
    private ArrayAdapter week_array;
    private GridLayout gridLayout;
    private ImageButton add_imageButton;
    private int width;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setToolbar();
        setWeek_spinner(week_spinner,week_array);
        setGridLayout(gridLayout);
        setAdd_imageButton(add_imageButton);
        getWidth();
        add_content(1,1,"测试margin使用");
        add_content(2,1,"测试使用得到,以后这里由course类获取用户的输入信息");
        add_content(2,2,"测试使用得到,以后这里由course类获取用户的输入信息");
        add_content(2,3,"测试使用得到,以后这里由course类获取用户的输入信息");

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
    public void add_content(int col,int row,String text){
        //每节课的那一个总的卡片布局放在course_card.xml文件中,这里将其绑定到变量course_card_view上
        View course_card_view= LayoutInflater.from(this).inflate(R.layout.course_card,null);

        //第一列节数列占比中较小
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
    }

    //获取屏幕分辨率的方法,借此将卡片等宽放置,长度预先设定死的,所以暂且只需要设定宽度
    public void getWidth(){
        DisplayMetrics metrics = new DisplayMetrics();
        metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //测试时的使用提示
        String a_width = String.valueOf(width);
        Toast toast = Toast.makeText(MainActivity.this,a_width,Toast.LENGTH_SHORT);
        toast.show();
        this.width=width;
    }

    //设置toolbar的方法,不会写,放弃了,预计将spinner(选择当前周)放在左边标题旁边,加号图标按钮放在最右边
    public void setToolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);




    }

    //这个方法中缺少从editText获取值的方法,待补充,当前思路,添加时暂时先在后台逻辑直接添加到页面上,下次启动时从数据库读取时再一次性重新加载
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
                //这里应该调用插入到数据库的方法,并且也应让其显示出来
                Insert_sql();
                //应该有一个重新从数据库中检索数据的方法
                show_sql();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create();
        builder.show();


    }

    //插入数据库的方法
    public void Insert_sql(){
        Toast toast = Toast.makeText(MainActivity.this,"测试啊这是测试",Toast.LENGTH_SHORT);
        toast.show();


    }

//    从数据库查询的方法
    public void show_sql(){

    }



}
