package flink.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Iterator;
import java.util.Properties;

/**
 * WindowFunction_Apply: 通过 apply 方法实现 聚合 单词统计。
 * */
public class WindowFunction_Apply {
    public static void main(String[] args) throws Exception {
        // 1.获取flink流计算的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //Kafka props
        Properties properties = new Properties();
        //指定Kafka的Broker地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //指定组ID
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "g1");
        //如果没有记录偏移量，第一次从最开始消费
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 2.从kafka读取数据
        FlinkKafkaConsumer011<String> kafkaSource = new FlinkKafkaConsumer011<>("test2",new SimpleStringSchema(), properties);

        DataStreamSource<String> stringDataStreamSource = env.addSource(kafkaSource);
        stringDataStreamSource.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return new Tuple2<>(s,1);
            }
        }).keyBy(0)
                .timeWindow(Time.seconds(5))                    //滚动窗口:5秒是一个时间窗口
                .apply(new WindowFunction<Tuple2<String, Integer>, Tuple2<String, Integer>, Tuple, TimeWindow>() {
                    @Override
                    public void apply(Tuple s, TimeWindow timeWindow, Iterable<Tuple2<String, Integer>> iterable, Collector<Tuple2<String, Integer>> collector) throws Exception {
                        Integer value=0;
                        String key="";
                        for(Tuple2<String,Integer> t1:iterable)
                        {
                         value=value+t1.f1;
                         key=t1.f0;
                        }
                        collector.collect(new Tuple2<>(key,value));
                    }
                }).print();
        //4.启动流计算
        env.execute();
    }
}
