package 基础语法;
/**
 * 选择判断: if ..else ; if ..else if  else     <br/>
 *        :switch(表达式){ case value1:表达式;break; default:表达式;}
 * */
public class 选择判断 {
    private int a=10;
    public void if_PanDuan(){
        if (a>10)
        {
            System.out.println(a+">10");
        }else {
            System.out.println(a+"<=10");
        }
        if (a>10)
        {
            System.out.println(a+">10");
        }else if (a==10){
            System.out.println(a+"==10");
        }else {
            System.out.println(a+"<10");
        }
    }
    public void switch_Case(){
        switch (a){
            case 1:
                System.out.println("1");break;
            case 2:
                System.out.println("2");break;
            case 3:
            case 4:
                System.out.println("3,4");break;
            case 10:
                System.out.println(10);break;
            default:
                System.out.println("gt");
        }
    }
    public static void main(String[] args) {
        选择判断 k1=new 选择判断();
        k1.switch_Case();
    }
}
