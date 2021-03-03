package 数据结构与算法.线性结构.队列;

import java.util.Scanner;

/**
 * 类介绍: 使用环形数组生成队列
 * */
public class QueueArr {
    private int front=0;//初始化front
    private int rate=0; //初始化rate
    private int[] arr; //声明一个环形数组
    private int maxSize;
    /**初始化队列数组长度*/
    public QueueArr(int len){
        arr=new int[len];
        maxSize=len;
    }
    /**
     * 判断队列是否为空
     * */
    public boolean isNUll(){
        return (front==rate)?true:false;
    }
    /**
     * 判断队列是否已满
     * */
    public boolean isFull(){
        return (((rate+1)%maxSize)==front)?true:false;
    }
    /**
     * 入队操作
     * */
    public void addQueue(int data){
        if(isFull()){
            System.out.println("队列已满!");return;
        }else{
            arr[rate]=data;
            rate++;
        }
    }
    /**
     * 出队操作
     * */
    public void deleteQueue(){
        if(isNUll()){
            System.out.println("队列是空的!");return;
        }else {
            int outData=arr[front];
            front=(front+1)%maxSize;
            System.out.println(outData+"已出队");
        }
    }
    /**
     * 打印队列有效数据
     * */
    public int validData(){
        int validNum=(rate-front+maxSize)%maxSize;
        return validNum;
    }
    /**遍历有效队列数据元素*/
    public void showData(){
        for(int i=front;i<front+validData();i++){
            System.out.print(arr[i%maxSize]+"|");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        QueueArr queueArr=new QueueArr(7);
        System.out.println("操作队列基本操作");

        while(true){
            System.out.println("s:打印有效数据个数:");
            System.out.println("a:入队");
            System.out.println("d:出队");
            System.out.println("q:退出");
            Scanner scanner=new Scanner(System.in);
            String str=scanner.next();
            switch (str){
                case "s":
                    queueArr.showData();
                    break;
                case "a":
                    queueArr.addQueue(scanner.nextInt());
                    break;
                case "d":
                    queueArr.deleteQueue();
                    break;
                case "q":
                    return ;
            }
        }
    }
}
