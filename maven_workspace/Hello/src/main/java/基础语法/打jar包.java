package 基础语法;
/**
 * 把工程打jar包有2种方式. 1种是用idea手动打jar包。此处不多说.第二种是使用命令.
 * 使用命令打jar包如下:
    1.进入class 根路径下.执行jar 命令
    D:\work\java\maven_workspace\Hello\target\classes>jar -cvf d:\\tmp\\ceshi.jar .
    2.打开ceshi.jar\META-INF\MANIFEST.MF
 添加主类: Main-Class: test.Hello
    3.执行jar文件 ：d:\tmp>java -jar ceshi.jar
    4.解压jar文件: jar -xvf ceshi.jar
 * */
public class 打jar包 {
}
