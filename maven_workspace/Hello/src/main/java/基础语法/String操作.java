package 基础语法;
/**
 * 拆箱: 对象类型转换为基础类型    Integer1.intValue();
 * 装箱: 基础类型转换为对象类型    String.valueOf(int1);
 * */
public class String操作 {
    public static void main(String[] args) {
//        String_Opt str1=new String_Opt();
//        String_Opt str2=new String_Opt();
//        str1.opt_Example();
//        str1.装箱拆箱();
//        String_Opt.stringBufferOpt();
        String_Opt.bytesToString();
    }
}
/**
 * String_Opt常用操作:
 * final 修饰的类不会被其他类继承!
 * */
final class String_Opt{
    String str=new String();                 //初始化:创建一个空字符串
    String str0=new String("123");   //初始化:创建一个"123"字符串
    char[] ch={'l','j','q'};
    String ch_to_str=new String(ch);        //初始化:使用char数组一个String对象
    String str1="zgk";  //对String对象初始化
    String str2="wjj";
    String str3=str1+str2;
    /**
     *   opt_Example: 1.String类函数日常操作
     *
     * */
    public void opt_Example(){
        System.out.println("str1+str2="+str3); //对两个String 对象进行拼接
        String str=new String("apply_no:NEW1022|loan_no:20110231");
        String str_bak=new String("apply_no:NEW1022|loan_no:20110231");
        /**
         * == 和 equals 区别: == 是比较内存地址 ;   equals 是比较 值
         * */
        System.out.println("str==str_bak"+(str==str_bak));
        System.out.println("str.equals(str_bak)"+str.equals(str_bak));
        String str1="";
        System.out.println("_字符第一次出现在:"+str.indexOf('_')); //返回_所在的位置
        System.out.println("_字符最后一次出现在:"+str.lastIndexOf('_')); //返回_最后一次所在的位置
        System.out.println("apply_no:NEW1022|loan_no:20110231 第4个位置对应的字符是:"+str.charAt(3));
        System.out.println("apply_no:NEW1022|loan_no:20110231 是否以apply_no开始:"+str.startsWith("apply_no"));
        System.out.println("apply_no:NEW1022|loan_no:20110231 是否以20110231结尾:"+str.endsWith("20110231"));
        System.out.println("apply_no:NEW1022|loan_no:20110231的长度:"+str.length());
        System.out.println(str.equals("apply_no:NEW1022|loan_no:20110231")); //两个字符串是否相等
        System.out.println("str1的长度:"+str1.length()+"|str1是否为空:"+str1.isEmpty());
        System.out.println("apply_no:NEW1022|loan_no:20110231 是否包含loan_no:"+str.contains("loan_no"));
        System.out.println("apply_no:NEW1022|loan_no:20110231转化为大写:"+str.toUpperCase());
        System.out.println("apply_no:NEW1022|loan_no:20110231转化为小写:"+str.toLowerCase());
        System.out.println("装箱操作:int-->String:"+String.valueOf(10));
        System.out.println("String转换为字符数组:"+str.toCharArray());
        System.out.println("把apply_no替换为cust_no:"+str.replace("apply_no","cust_no"));
        System.out.println("把|作为分隔符:"+str.split("\\|"));
        String[] arr_str=str.split("\\|");
        for(String s:arr_str){
            System.out.println(s);
        }
        System.out.println("截取索引6-8位置的字符串[6,8) 左闭右开:"+str.substring(6,8));
        System.out.println("去除首尾的空格:"+str.trim());
    }
    /**
     *   装箱拆箱: 对象类型转化为基础类型(拆箱);基础类型转化为对象类型(装箱).
     *
     * */
    public void 装箱拆箱(){
        Integer int1=123;
        System.out.println("拆箱操作:Integer 拆箱成int-->"+int1.intValue());
        String x="123";
        System.out.println("String拆箱为int:"+Integer.parseInt(x));  //静态类的parse方法一般都是拆箱.
        int int2=123;
        System.out.println("装箱操作:int装箱成String-->"+String.valueOf(int2));

    }
    /**
     * stringBufferOpt:StringBuffer类方法基本操作
     * StringBuffer[线程安全类,会有上锁解锁操作] VS String  [StringBuffer类分配的内存是可变的.String类分配的内存不可变的!]
     * StringBuffer [线程安全类,会有上锁解锁操作] vs StringBuilder  [线程不安全,无锁操作]
     * */
    public static void stringBufferOpt(){
        StringBuffer buffer=new StringBuffer();
        buffer.append("z1");
        buffer.insert(0,"z0");
        buffer.reverse();  //反转
        System.out.println("StringBuffer:"+buffer);

    }
    /**
     * 字节和String的转换
     * */
    public static void bytesToString(){
        String str="123";
        byte[] bytes=str.getBytes();
        String str1=new String(bytes);
        System.out.println("bytesToString:"+str1);
    }

}

