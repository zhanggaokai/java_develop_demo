package 基础语法;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 处理异常步骤:  定义异常,抛出异常,捕获异常
 * JAVA中的异常有2种：Error[系统异常,内存溢出];Exception[程序异常,数组索引超出边界]
 * 自定义异常 : 需要继承Excetion类或者Exception的子类

 * */
public class 异常 {
    /**
     * throw new 异常类  来抛出异常. 再用catch 来捕获异常
     * */
    public void 抛出异常(){
        try{
            File f1=new File("e:\\tmp.txt");
            if(!f1.exists()){throw  new FileNotFoundException("file not found!");}
        }catch (FileNotFoundException e){e.printStackTrace();}
    }
    public void 捕获自定义异常(int x) {
        try {
            if (x == 10) {
                throw new 自定义异常("参数不能等于10!");
            }
        }catch (自定义异常 e){e.printStackTrace();}
    }
    public static void main(String[] args) {
        异常 抛出异常1=new 异常();
        抛出异常1.抛出异常();
        异常 自定义异常1=new 异常();
        自定义异常1.捕获自定义异常(10);
    }
}
class 自定义异常 extends Exception{
    public 自定义异常(){super();}
    public 自定义异常(String message){super(message);}
}