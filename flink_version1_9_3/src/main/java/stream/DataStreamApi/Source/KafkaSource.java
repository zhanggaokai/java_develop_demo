package stream.DataStreamApi.Source;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;

import java.util.Properties;

public class KafkaSource {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        streamExecutionEnvironment.setParallelism(4); //设置并行度
        Properties properties=new Properties();
        properties.put("bootstrap.servers","localhost:9092");
        //指定消费者组
        properties.put("group.id","g1");
        DataStream<String> dataStream=streamExecutionEnvironment.addSource(new FlinkKafkaConsumer011<String>("test2",new SimpleStringSchema(),properties));
        dataStream.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                for(String str:s.split(",")){
                    collector.collect(new Tuple2<>(str,1));
                }
            }
        }).keyBy(0).sum(1).print();
        /**execute 方法 启动流 任务 */
        streamExecutionEnvironment.execute("wordcount");
    }
}
