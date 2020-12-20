package 基础语法;
/**
 * 1.java变量 区分大小写 <br/>
 * 2.对于常量[使用final修饰符的] 字母统一大写 AREA_GEO;对于变量 从第二个单词的首字母大写 area_Geo   <br/>
 * 3.变量分类: 基础类型[int,float,long,boolean];对象类型[Integer,Float,Long,Boolean]        <br/>
     装箱:基础类型转为对象类型[int a=10;String str=String.valueOf(a)]                            <br/>
     拆箱:对象类型转为基础类型[Integer x1=10;int i=x1.intValue();]
   4.类型转换: 自动类型转换[小精度转大精度 int a=10;long l=a;];
              强制类型转换[大精度转小精度      long l1=10l;int aa=(int)l1;]
 * */
public class 变量 {
    //定义一个常量,常量字母全大写
    private final int CONSTANT=10;
    public void 装箱拆箱(){

        int a=10;String str=String.valueOf(a);//装箱
        Integer x1=10;int i=x1.intValue(); //拆箱
    }
    public void 类型转换(){
        int a=10;long l=a;
        long l1=10l;int aa=(int)l1; // 强制类型转换
    }

    public static void main(String[] args) {


    }

}
