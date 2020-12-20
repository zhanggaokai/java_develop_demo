package kafka;
/**
 * 使用回调 返回partition和offset偏移量. 证明 消息发送 状态成功或失败!
 * 不指定分区和key的时候。生产者 通过轮询的方式放入到一个分区中.
 * */
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import java.util.Properties;
public class CallBackProducer {
    public static void main(String[] args) throws InterruptedException {
        Properties props=new Properties(); //相当于HashMap<String,String>. Properties自动识别=为map,value的分隔符
        //指定Kafka服务端的主机名和端口号
        props.put("bootstrap.servers","localhost:9092");
        //等待所有副本节点的应答:所有副本消息同步完毕 ,表示消息发送成功!
        props.put("acks","all");
        //消息发送最大尝试次数
        props.put("retries",0);
        //一批消息处理大小:16k
        props.put("batch.size",16384);
        //请求延时: 隔1毫秒  向 服务器 提交一下消息. [防止频繁请求服务器 造成 IO消耗]
        props.put("linger.ms",1);
        //发送缓存区内存大小
        props.put("buffer.memory",33554432);
        //key序列化
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer") ;
        //value序列化
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer") ;
        KafkaProducer<String,String> producer=new KafkaProducer<String, String>(props);
        for(int i=0;i<100;i++){
            //Thread.sleep(500);
            //插入test2 主题的1分区中.
            producer.send(new ProducerRecord<String, String>("test2",1,null,"partition-" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(recordMetadata!=null){
                        System.out.println(recordMetadata.partition()+"++++partition++++"+recordMetadata.offset());
                    }
                }
            });
        }
        producer.close();
    }
}
