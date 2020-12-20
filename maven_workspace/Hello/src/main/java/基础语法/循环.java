package 基础语法;

import java.util.Arrays;
import java.util.List;

/**
 * 1.循环方式:
             while(循环条件){执行体;} ;
             do{执行体;} while(循环条件) ;
             for(int i=0;i<10;i++){执行体;};
             for(String str:Collection<String>)   循环遍历集合对象
 * 2.break [中断循环]    continue  [跳出本次循环]
 * */
public class 循环 {
    public static void main(String[] args) {
        int i=3;
        //while循环遍历
        while(i<5){
            System.out.println(i);i++;
            if (i==4){break;}else {continue;}
        }
        //do while 循环遍历
        do{
            System.out.println(i);i++;
        }while (i<5);

        for(int x=0;x<i;x++){
            System.out.println(x);
        }

        String[] str={"str1","str2"};
        List<String> l1= Arrays.asList(str);
        //for 集合遍历
        for(String s:l1){
            System.out.println(s);
        }
    }
}
