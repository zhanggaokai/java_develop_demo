package kafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * ProducerSendJson: 定义一个生产者.用于发送json数据
 * */
public class ProducerSendJson {
    /**用于生成json数据*/
    public  static String ObjectToJson(Message message){
        String jsonStr= JSON.toJSONString(message);
    return jsonStr;
    }
    public static void main(String[] args) throws InterruptedException {
        Properties props=new Properties(); //相当于HashMap<String,String>. Properties自动识别=为map,value的分隔符
        //指定Kafka服务端的主机名和端口号
        props.put("bootstrap.servers","localhost:9092");
        //等待所有副本节点的应答
        props.put("acks","all");
        //消息发送最大尝试次数
        props.put("retries",0);
        //一批消息处理大小
        //props.put("batch.size",16384);
        //请求延时
        props.put("linger.ms",1);
        //发送缓存区内存大小
        //props.put("buffer.memory",33554432);
        //key序列化
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer") ;
        //value序列化
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer") ;
        // 注册拦截器
        List<String> inlist=new ArrayList<>();
        //inlist.add("kafka.TimeInterceptor");
        inlist.add("kafka.CounterInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,inlist);
        KafkaProducer<String,String> producer=new KafkaProducer<String, String>(props);

        for(int i=0;i<3000;i++){
            Thread.sleep(50);
            String str=String.valueOf(System.currentTimeMillis());
            Message m1=new Message("json1",i,str);
            String jsonstr=ProducerSendJson.ObjectToJson(m1);
            producer.send(new ProducerRecord<String, String>("test2",0,"aa",jsonstr));
        }
        //producer.flush();
        producer.close();
    }
}
/**用于定义Json对象*/
class Message{
    private String name;
    private int age;
    private String currTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCurrTime() {
        return currTime;
    }

    public void setCurrTime(String currTime) {
        this.currTime = currTime;
    }
    public Message(){}
    public Message(String name,int age,String currTime){
        this.name=name;
        this.age=age;
        this.currTime=currTime;
    }

}