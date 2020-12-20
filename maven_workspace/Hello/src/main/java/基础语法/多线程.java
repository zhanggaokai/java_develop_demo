package 基础语法;

import java.lang.Thread;
import java.io.*;
import java.util.concurrent.locks.Lock;

/**
 *线程个数和cpu核数： 假如 你有10块玉米地. 有1个收割机.  收割机就是cpu,你就是线程1, 收割机一块一块的去收个玉米地
   假如 你有10块玉米地.有5台收割机。你有10个人收玉米。那就是有10个线程,5个cpu.  1个cpu负责收2个线程的地.
 *实现线程的方式: 1. implements Runnable  2. extends Thread
               采用Runnable实现的应用程序比较多[因为实现Runnable同时可以进行继承]
 *前台线程和后台线程: 只要进程中还有一个前台线程在运行，程序就会等待前台线程执行完退出
                   如果没有前台线程,只有后台线程在运行。程序会直接退出. 类似于[ nohup sh & ]
                   设置后台线程:thread3.setDaemon(true); //把thread3设置为后台线程
 后台线程又名 守护线程.
 *线程要想进入运行状态，必须首先得到CPU资源。
 *线程优先级:      通过设置优先级，可优先获取CPU资源.   优先级1-10 。 10的优先级最高.1的最低、 thread1.setPriority(1);
 * 线程休眠: Thread.sleep(3000); 不释放锁 //线程休眠3000毫秒.在3000毫秒内由运行状态进入阻塞状态。3000毫秒后由阻塞状态进入就绪状态
 * 线程让步：将线程所占的cpu 资源让出去.和其他线程重新竞争资源.  Thread.yield(); // 让出cpu资源。重新竞争 .[由运行状态进入就绪状态]

 * 线程插队：等待插队线程执行完,才能执行其他线程操作. thread1.join(1000); //thread1线程插队1000毫秒
 * 线程同步: 当多个线程同时操作同一个资源的时候。如果不设置线程同步,会导致呀 输出的结果不准确嘛！
 例如: 3个线程[A,B,C]同时去收割同一块玉米地. A 线程已经收割好咧.如果不和B,C线程同步。那么B,C线程就会再次收割一遍嘛
  1.synchronized 修饰方法来进行代码块的锁定。 [表示悲观锁]
  2.synchronized(lock){代码块} 对{}内的代码块上锁.
 *线程通信: wait() 和notify()
     wait是将线程从运行状态转为阻塞状态嘛，会释放锁资源的!  notify() 将线程从阻塞状态转为就绪状态嘛!
 * */
public class 多线程 {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        MyThread myThread=new MyThread();
        Thread thread1=new Thread(myThread,"t1");
        Thread thread2=new Thread(myThread,"t2");
        Thread thread3=new Thread(myThread,"t3");
        thread1.setPriority(1); //设置thread1优先级为1
        thread2.setPriority(4); //设置thread2优先级为4
        //thread1.setDaemon(true);thread2.setDaemon(true);thread3.setDaemon(true); //把thread2和thread3设置为后台线程.
        thread1.start();thread2.start();thread3.start();
//        thread1.join(1000); //thread1线程插队1000毫秒
        System.out.println("-----------------");
//        Thread.sleep(3000); //线程休眠3000毫秒
    }
}
/**
 * MyThread 介绍: (1)Runnable 线程类的实现; (2)synchronized 悲观锁的实现
 * */
class MyThread implements Runnable {
    MyThread() throws FileNotFoundException {
    }

    FileInputStream fis = new FileInputStream("d:\\tmp\\daodejing.txt");
    InputStreamReader isr = new InputStreamReader(fis);
    BufferedReader br = new BufferedReader(isr);
    String line;
    int i = 0;
    boolean bl = true;

    /**
     * synchronized 修饰方法 表示悲观锁.
     * */
    public synchronized void read_line(BufferedReader br) throws IOException, InterruptedException {
        line = br.readLine();
        Thread.sleep(50);
        if (line != null) {
            bl = true;
            i++;
            Thread x = Thread.currentThread();
            String thread_name = x.getName();
            if (i == 10) {
                Thread.yield(); // 让出cpu资源 重新竞争
            }
            System.out.println("线程" + thread_name + "读取" + line);
        } else {
            bl = false;
        }
    }
    /**
     * synchronized(lock){代码块}  表示 悲观锁.
     * */
    public Object lock=new Object(); //定义个代码块的锁
    int j=10;
    public  void 悲观锁2(){
        while (j>0) {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "悲观锁2_输出变量:" + (j--));

            }
        }
    }

        public void run() {
            悲观锁2();
            while (bl == true)
            {
                try
                {
                    read_line(br);
                } catch (IOException | InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

        }
}
/**
 *
 * */
class 线程同步 implements Runnable{
    public void run(){

    }
}