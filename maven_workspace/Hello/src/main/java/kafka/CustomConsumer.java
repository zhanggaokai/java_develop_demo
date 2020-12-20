package kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * 定义一个消费者. partition 满 1000条。 消费者才会拉取消费
 * */
public class CustomConsumer {
    public static void main(String[] args) {
        Properties props=new Properties();
        //定义kafka服务器地址.
        props.put("bootstrap.servers","localhost:9092");
        //指定消费者组
        props.put("group.id","g1");
        //是否自动确认offset
        props.put("enable.auto.commit","true");
        //自动确定offset的时间间隔
        props.put("auto.commit.interval.ms","1000");
        //key的反序列化类
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer") ;
        //value的反序列化类
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer") ;
        //定义consumer
        KafkaConsumer<String,String> consumer=new KafkaConsumer<String, String>(props);
        //设定 释放消费者资源. 当异常退出时,释放资源!
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if(consumer!=null){
                    consumer.close();
                }
            }
        }));
        //消费者订阅topic.可同时订阅多个topic
        consumer.subscribe(Arrays.asList("test2"));
        while (true)
        {
            //读取数据,读取超时时间为100ms
            ConsumerRecords<String,String> records=consumer.poll(100);
            for(ConsumerRecord<String,String> record:records){
                System.out.println("offset:"+record.offset()+"key:"+record.key()+"value:"+
                        record.value());
            }
        }
    }
}
