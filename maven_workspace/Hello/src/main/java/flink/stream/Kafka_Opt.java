package flink.stream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * flink整合kafka
 * 以kafka作为source加工数据后 再次写入kafka.
 * */
public class Kafka_Opt {
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
        //对kafka传来的Json数据进行解析:
        stringDataStreamSource.map(new MapFunction<String, Tuple3<String,String,String>>() {
            @Override
            public Tuple3<String,String,String> map(String s) throws Exception {
                JSONObject jsonObject=JSON.parseObject(s);
                String name=jsonObject.getString("name");
                String age=jsonObject.getString("age");
                String currTime=jsonObject.getString("currTime");
                return new Tuple3<String,String,String>(name,age,currTime);
            }
        }).assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple3<String,String,String>>() {
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
            public long extractTimestamp(Tuple3<String,String,String> s, long l) {
                String str_event_time=s.f2;
                Long l_event_time=Long.parseLong(str_event_time);
                currentMaxTimestamp=Math.max(l_event_time,l);  //比对当前时间和上个消息的时间.
                System.out.println("当前时间:"+sf.format(System.currentTimeMillis())+
                        "事件时间:"+sf.format(l_event_time)+"水印时间:"+sf.format(getCurrentWatermark().getTimestamp()));
                return currentMaxTimestamp;
            }
        })
                .print();

        //3.调用Sink
        stringDataStreamSource.print();
        //把test2主题数据 放入click主题中
        FlinkKafkaProducer011<String> produce=new FlinkKafkaProducer011<String>("localhost:9092","click",new SimpleStringSchema());
        stringDataStreamSource.addSink(produce);
        //4.启动流计算
        env.execute();
    }
}
