package 基础语法;

import java.util.Arrays;

/**
 * 声明数组: int[] a={1,2,3};  //静态 赋值
 *         int[] a1=new int[3]; a1[0]=1;a1[1]=1;a[2]=2;  //动态赋值
 * */
public class 数组 {
    public static void 声明数组(){
        int[] a={1,2,3};
        int[] a1=new int[3];
        a1[0]=10;
        a1[1]=2;
        a1[2]=3;
        Arrays.sort(a1); //数组成员排序
        for(int x:a1){
            System.out.println(x);
        }
    }
    public static void main(String[] args) {

        数组.声明数组();
    }
}
