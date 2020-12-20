package 基础语法;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
/**
 * 1.介绍Properties操作
 * */
public class 集合类操作 {
    public static void main(String[] args) throws IOException {
        propertiesOpt();
    }
    /**Properties 操作:
     * Properties和Map区别： Map是线程不安全的;Properties是线程安全的
     * */
    public  static void propertiesOpt() throws IOException {
        Properties properties = new Properties();
        properties.put("address", "localhost:9092"); //相当于 address=localhost:9092
        FileWriter fw=new FileWriter("d:\\tmp\\input\\1.txt");
        //将peroperties保存到fw流中
        properties.store(fw,null);
        fw.close();
        Properties properties1 = new Properties();
        //从文件中读取property属性
        FileReader fr=new FileReader("d:\\tmp\\input\\1.txt");
        properties1.load(fr);
        Set<String> proKey=properties1.stringPropertyNames();
        for(String key:proKey)
        {
            String value=String.valueOf(properties1.get(key));
            System.out.println("key:"+key+"|value:"+value);
        }
        fr.close();
    }
}
