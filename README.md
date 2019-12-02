乐程课程表
    
    
    
    运行逻辑:
        1.启动时先加载组件方法,并且查询数据库,查询数据库的时候会用到添加课程卡片的方法
        2.添加卡片的方法会从数据库中读取课程信息,进行循环添加
        3.点击添加的图片按钮时,弹出输入课程信息的dialog,点击dialog 的确定按钮时,获取输入的信息,并从course构造类中走一遍(用以日后扩展)插入数据库中,
        4.第3步插入数据库之后,立刻重新执行数据库的查询,并且插入卡片
        备注:添加卡片的方法在查询数据库的方法中被调用

    运行依赖:
        cardview-v7:28.0.0(需手动安装)
        minSdkVersion 15 ==>>  minSdkVersion 21 (build.gradle文件)
        可能需要添加的依赖(一般不用添加) com.android.support:appcompat-v7:25.3.1
         

    提交记录表:
        1.2019-11-27 9:25 git初次创建提交测试
        2.2019-11-30 惯例提交
        3.2019-12-02 13:01 课程表基本框架基本完成,仅剩细节之处需要修理
        4.2019-12-02 17:21 添加了详细信息显示,添加了删除课程的方法,更新课程的方法暂未完善
        
    参考链接记录:
        Android开发：使用CardView实现卡片式设计 https://www.jianshu.com/p/31f163f5c9d9 
        CardView完全解析和使用 https://blog.csdn.net/yushuangping/article/details/89358983 
        Android GridLayout 动态添加子控件 + 平均分配空间 https://blog.csdn.net/sunsteam/article/details/69486658
        TextView.setWidth()无效 https://blog.csdn.net/dbmonkey/article/details/88030854
        ActionBar和Toolbar的基础使用 https://blog.csdn.net/xingzhong128/article/details/79696467 
        SQLiteDatabase之execSQL()和rawQuery() https://blog.csdn.net/u014167212/article/details/38663825
        Android Cursor 的使用细节 https://blog.csdn.net/zhw0596/article/details/80973268
        
        
       