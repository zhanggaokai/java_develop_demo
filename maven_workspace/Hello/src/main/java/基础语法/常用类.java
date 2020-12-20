package 基础语法;

import org.omg.SendingContext.RunTime;

import java.io.IOException;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class 常用类 {
    public static void main(String[] args) throws IOException {
        //Opt_System.opt_1();
        try{
        //Opt_Runtime.opt_1();
//            Opt_Math.opt_1();
//            Opt_Random.Opt_1();
            Opt_Date.opt_1();
        }catch (Exception e){e.printStackTrace();}

    }
}
/**
 * Opt_System:System类常用操作
 * */
class Opt_System{
    public static void opt_1(){
        System.out.println(System.currentTimeMillis()); //返回以毫秒为单位的系统时间
        System.gc(); //进行垃圾回收
        System.out.println("系统属性:"+System.getProperties());
        Properties properties =System.getProperties();
        Enumeration en1=properties.propertyNames();
        while(en1.hasMoreElements()){
            String key=(String)en1.nextElement();
            System.out.println(key+"对应的系统属性:"+System.getProperty(key));
        }

        System.exit(0); //退出java虚拟机 0:正常退出  非0:异常退出
    }
}
/**
 * Opt_Runtime: Runtime类:主要包含了一些java虚拟机的信息.Runtime类是个单实例.
 * */
class Opt_Runtime{
    public static void opt_1() throws IOException, InterruptedException {
        Runtime rt=Runtime.getRuntime(); //生成一个Runtime对象
        Process p=rt.exec("notepad.exe"); //执行一个操作系统命令notepad.exe,生成一个进程对象
        Thread.sleep(10000); //睡眠10秒
        p.destroy();   //杀掉进程
    }
}
/**
 * Opt_Math : 数学相关的一些操作.
 * */
class Opt_Math{
    public static void opt_1(){
        int abs=Math.abs(-10);  //绝对值
        System.out.println("生成一个大于等于0小于1的随机值:"+Math.random());
    }
}
/**
 * Opt_Random : 随机数操作
 * */
class Opt_Random{
    public static void Opt_1(){
        Random rd=new Random();//不指定种子，每次产生的随机数不一样
        System.out.println("不指定种子:"+rd.nextInt(2)); //[0,2)  左闭右开
        Random rd1=new Random(100);//指定种子,每次产生的随机数是一样的
        System.out.println("指定种子100:"+rd1.nextInt(10));
        System.out.println("随机生成true/false:"+rd.nextBoolean());
    }
}
/**
 * 日期类 操作: Date类[生成日期类型],Calendar[日期运算],DateFormat类  [进行日期格式化]
 * */
class Opt_Date{
    public static void  opt_1() throws ParseException {
        Date dt=new Date();//生成系统日期
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cd=Calendar.getInstance();
        cd.setTime(dt);
        cd.add(Calendar.DAY_OF_MONTH,-1);  //减1天
        dt=cd.getTime();
        String strDt=sf.format(dt); // 格式化date格式为String yyyy-MM-dd格式
        System.out.println("Date-1天格式化为String:"+strDt);
        String curr_dt="2020-10-31";
        System.out.println("str转为date:"+sf.parse(curr_dt));
    }
}