package test;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class Hello {

    public static void main(String[] args) throws InterruptedException, IOException {
        propertiesOpt();
    }
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
    public static void time_Elapsed() throws InterruptedException {
        long time1=System.currentTimeMillis();
        System.out.println(time1);
        Thread.sleep(20);
        long time2=System.currentTimeMillis();
        System.out.println("停留了"+(time1)+"毫秒");
    }
}
