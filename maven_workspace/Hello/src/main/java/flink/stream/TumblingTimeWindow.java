package flink.stream;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

/**
 * 介绍流处理 的3种窗口类型:
                        翻滚窗口Tumbling 介绍: 窗口之间的数据无重合 .   窗口时间:10秒.计算时间:10秒.  每10秒 处理一次
                        滑动窗口slide    介绍: 窗口之间可能有数据重合.   窗口时间:10秒.计算计算:2秒。  每隔2秒统计一下 过去10秒的数据
                        【计算时间<窗口时间,会导致 数据算重复。计算时间大于窗口时间,会导致 数据少算】
 *countWindow :按照消息个数来划分窗口大小
 * */
public class TumblingTimeWindow {
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
        //5秒一个翻滚窗口 进行wordcount 。.timeWindow(Time.seconds(5))
        //每2秒统计过去5秒的数据.  .timeWindow(Time.seconds(5),Time.seconds(2))
        stringDataStreamSource.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return new Tuple2<>(s,1);
            }
        }).keyBy(0)
                //.timeWindow(Time.seconds(5),Time.seconds(2))  //滑动窗口: 每2秒统计一次.时间窗口:5秒
                .timeWindow(Time.seconds(5))                    //滚动窗口:5秒是一个时间窗口
        //.countWindow(500)                //滚动窗口:每500个消息处理一次
        //  .countWindow(500,100)  //每100个消息滑动一次，处理过去500个消息
                .sum(1)
                .print()
                .setParallelism(1); //设置并行度
        //4.启动流计算
        env.execute();
    }
}
