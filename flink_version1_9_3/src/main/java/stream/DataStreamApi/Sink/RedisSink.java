package stream.DataStreamApi.Sink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

/**
 * Redis是用的Bahir 连接器. 移交redis
 * */
public class RedisSink {
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


        streamExecutionEnvironment.execute("kafkaSink");
    }
}
