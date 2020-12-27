package test;

public class Hello extends T{
    public static final int a=10;
    public static final int[] arr={1,2,3};
    public void printA(final int i){
        int y=i;
        y=y+50;
        System.out.println(y);
    }
    public static void main(String[] args)  {
        Hello hello=new Hello();

    }

}

class T{
    public final void prin(){
        System.out.println("-----");
    }
}