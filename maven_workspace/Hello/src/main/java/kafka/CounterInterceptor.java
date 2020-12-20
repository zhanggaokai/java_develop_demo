package kafka;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
/**
 * CounterInterceptor : 消息 计数
 * */
public class CounterInterceptor implements ProducerInterceptor<String,String> {
    private long successCount=0;
    private long errorCount=0;
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if(e==null){
            successCount++;
        }else{
            errorCount++;
        }
    }

    @Override
    public void close() {
        System.out.println("发送成功的消息:"+successCount);
        System.out.println("发送失败的消息:"+errorCount);
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
