package flink.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Properties;

/**
 * WaterMark : 水印线
 * 时间类型: EventTime[事件时间:数据实际产生的时间];
 *          IngestionTime[摄入时间] 数据进行flink的时间;
 *          ProcessTime[处理时间]  flink 算子计算对应的时间
 * */
public class WaterMark {
    public static StreamExecutionEnvironment ev;
    public static FlinkKafkaConsumer011<String> kafkaSource ;
    public static DataStreamSource<String> ds;
    /**
     * eventTimeOpt:设置时间类型.
     * */

    public static void main(String[] args) throws Exception {

        //ev.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime); //设定为接入时间
        //ev.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime); //设定为处理时间
        ev=StreamExecutionEnvironment.getExecutionEnvironment();//生成流环境上下文
        ev.setStreamTimeCharacteristic(TimeCharacteristic.EventTime); //设定为时间事件类型
        Properties properties = new Properties();
        //指定Kafka的Broker地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //指定组ID
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "g1");
        //如果没有记录偏移量，第一次从最开始消费
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 2.从kafka读取数据
        kafkaSource = new FlinkKafkaConsumer011<>("test2",new SimpleStringSchema(), properties);
        ds=ev.addSource(kafkaSource);
        //添加水印
        ds.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<String>() {
            Long currentMaxTimestamp = 0L;
            final Long delayTime = 2000L;// 延时时间2s
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            @NotNull
            @Override
            //获取水印时间
            public Watermark getCurrentWatermark() {
                Watermark wm=new Watermark(currentMaxTimestamp-delayTime);
                return wm;
            }

            @Override
            //抽取当前时间
            public long extractTimestamp(String s, long l) {
                String str_event_time=s.split(",")[1];
                Long l_event_time=Long.parseLong(str_event_time);
                currentMaxTimestamp=Math.max(l_event_time,currentMaxTimestamp);
                System.out.println("当前时间:"+sf.format(System.currentTimeMillis())+
                        "事件时间:"+sf.format(l_event_time)+"水印时间:"+sf.format(getCurrentWatermark().getTimestamp()));
                return currentMaxTimestamp;
            }
        }).map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return  new Tuple2<>(s.split(",")[0],1);
            }
        }).keyBy(0).timeWindow(Time.seconds(5)).sum(1).print();
        //启动执行
        ev.execute();
    }
}
