package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * CustomProducer: 定义一个生产者.
 * */
public class CustomProducer {
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

        for(int i=0;i<1000;i++){
            Thread.sleep(5);
            String str="zhangsan"+","+i+","+String.valueOf(System.currentTimeMillis());
            producer.send(new ProducerRecord<String, String>("test2",0,"aa",str));
        }
        //producer.flush();
        producer.close();
    }
}