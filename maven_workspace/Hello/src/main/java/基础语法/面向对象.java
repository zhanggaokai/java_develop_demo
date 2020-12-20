package 基础语法;

/**
 * 面向对象的三大特性:
                   封装:把对象的属性和方法封装起来.
                   继承:继承[extends].[目前一个子类只能继承1个父类。不过可以实现多个父接口].子类拥有父类共有权限的属性和方法.
                   多态:针对父类的属性和方法。子类可以赋予不同的含义和实现方式.多态只是实现方式有所不同。参数变化或者返回类型变化属于方法重载,不属于多态
* 使用this表示对象.  super表示父类  ;     super() 调用父类无参构造方法,this() 调用子类无参构造.
 * 垃圾回收机制:   通过调用System.gc()方法通知系统进行垃圾回收
                 在系统进行回收垃圾前,会调用finalize()方法表示 垃圾将要被回收
 *static 关键词:   表示静态变量和静态方法.在内存中只有一份。可以为所有实例对象共用！节省内存. 同时实例对象无法调用只能通过类名.方法名[属性]调用
 *静态块: static {执行体;}  静态块只执行一次!
 * 单实例模式: 只能实例化一个对象.  需要把构造方法给私有化. 将实例化对象 设置为Single 类型
 * final 关键词: 修改类 :类不能被继承
                修饰变量:只能赋值一次.
                修饰方法:方法不能被子类重写
 抽象 abstract :当我们对某一个方法不知道怎么实现的时候,可以声明为抽象方法。含有抽象方法的类必须声明称抽象类.
               抽象类不能被实例化.继承抽象类的子类必须实现父类的抽象方法.
               抽象类中可以包含抽象方法和非抽象方法
 接口 interface:接口内的方法都是抽象的[public abstract]; 属性是 public static final [静态常量],属性必须初始化赋值.
                接口和抽象类一样 不能被实例化。 只能通过子类去实现接口中的方法. 不过一个子类可以实现多个接口.
                实现接口: implements
                接口继承接口 ：extends
 *  访问修饰符: protected : 被他修饰的属性或方法只能被同package内的类或者他的子类访问
              public   :任何类都能访问
              default  :被他修饰的属性或方法只能被同package内的类访问
              private  :只能被自己访问
 * */
interface  接口{
    void 方法1();
    void 方法2();
    int 属性=10;
}
interface  接口1{
    void 方法11();
    void 方法12();
}
interface 继承接口 extends 接口,接口1{}
class 实现接口 implements 接口{
    public void 方法1(){
        System.out.println("实现接口方法1");
    }
    public void 方法2(){
        System.out.println("实现接口方法2");
    }
}
abstract class  抽象类 {
    public int age;
    public void print_Age(){
        System.out.println(age);
    }
    public abstract void set_Age();
}
class Parent1 extends 抽象类{
    public Parent1(){
        System.out.println("我是父类构造！");
    }
    public  void set_Age(){
        System.out.println("实现抽象方法!");
    }
    public int age=10;
    public static String name;
    public static void print_Name(){
        System.out.println(name);
    }
    public void print_Age(int x)
    {
        System.out.println(x);
    }
}
/**
 * 单实例 类
 * */
class SingleInstance1{
    private SingleInstance1(){}
    public  static final SingleInstance1 instance=new SingleInstance1();

}
public class 面向对象 extends Parent1{
    //重写toString方法
    public String toString()
    {
        return "重写toString方法";
    }
    public 面向对象(){
        super();
        System.out.println("我是子类构造!我调用了父类构造");

    }
    public int age=20;
    public void print_Age() {
        System.out.println("父类的age:"+super.age+"子类的age:"+this.age);
    }
    /**
     * 多态的实现
     * */
    public void print_Age(int x){
        System.out.println("多态:"+x);
    }
    public void finalize(){
        System.out.println("垃圾将回收！内存即将释放！");
    }
    /**
     * 垃圾回收
     * */
    public void revoke_LaJi() throws InterruptedException {
        System.out.println("启动垃圾回收");
        Parent1 t1=new 面向对象();
        Parent1 t2=new 面向对象();
        t1=null;
        t2=null;
        System.gc();
        Thread.sleep(10000);

    }
    public static boolean sex;
    /**
     * 静态块:只执行一次!
     * */
    static {
        System.out.println("面向对象类被加载!");
    }

    public static void main(String[] args) throws InterruptedException {
        面向对象 t1=new 面向对象();
        System.out.println(t1.toString());
        t1.print_Age();
//        面向对象.sex=true;
//        面向对象.sex=false;
//        System.out.println(面向对象.sex);
        //  //获取 单实例 对象
//        SingleInstance1 single1=SingleInstance1.instance;
        System.out.println("----");

    }
}
