package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    //定义的全局变量
    private Spinner week_spinner;
    private ArrayAdapter week_array;
    private GridLayout gridLayout;
    private ImageButton add_imageButton;
    private int width;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWeek_spinner(week_spinner,week_array);
        setGridLayout(gridLayout);
        setAdd_imageButton(add_imageButton);
        getWidth();
        add_content(1,1,"测试margin使用");
        add_content(2,1,"测试使用得到,以后这里由course类获取用户的输入信息");
        add_content(2,2,"测试使用得到,以后这里由course类获取用户的输入信息");
        add_content(2,3,"测试使用得到,以后这里由course类获取用户的输入信息");

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
    private void add_content(int col,int row,String text){
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


}
