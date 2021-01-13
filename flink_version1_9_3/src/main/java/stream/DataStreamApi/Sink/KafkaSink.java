package stream.DataStreamApi.Sink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
/**
 * 向kafka写入数据
 * 端口号解释: 9092 kafka ; 2181 zookeeper
 * */
public class KafkaSink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        String path="D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\Word.txt";
         //读取文本数据源
        DataStream<String> dataStream=streamExecutionEnvironment.readTextFile(path);
        DataStream<String> filterStream=dataStream.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String s) throws Exception {
                if(s.startsWith("h"))
                {return true;}
                else {return false;}
            }
        });
        filterStream.addSink(new FlinkKafkaProducer011<String>("localhost:9092","test1",new SimpleStringSchema()));
        streamExecutionEnvironment.execute("kafkaSink");
    }
}
