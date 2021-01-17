package stream.DataStreamApi.winodw;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import redis.clients.jedis.Tuple;

/**
 * 实现 窗口操作步骤:1.需要使用keyby进行数据分发;
 *                2.设定window类型: 滚动时间 or 滑动时间 or 会话时间  or 滚动计数 or 滑动计数
 *                3.设定时间语义:事件时间 or process 时间
 *
 * */
public class WindowTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
//        String path="D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\Word.txt";
//         //读取文本数据源
//        DataStream<String> dataStream=streamExecutionEnvironment.readTextFile(path);
        DataStream<String> dataStream=streamExecutionEnvironment.socketTextStream("localhost",60000);
        DataStream<Tuple2<String,Integer>> mapStream=dataStream.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return new Tuple2<>(s,1);
            }
        });
        mapStream.keyBy(0)
        .timeWindow(Time.seconds(5))  //设定滚动窗口,窗口大小5秒
//        .timeWindow(Time.seconds(10), Time.seconds(2)) //设定一个2秒滑动一次,10秒窗口大小的滑动窗口
//        .window(EventTimeSessionWindows.withGap(Time.minutes(5)))  //设定一个会话窗口
//        .countWindow(10,2)     //计数滑动窗口
//        .countWindow(10)   //计数滚动窗口
        .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> stringIntegerTuple2, Tuple2<String, Integer> t1) throws Exception {
                return new Tuple2<String,Integer>(stringIntegerTuple2.f0,stringIntegerTuple2.f1+t1.f1);
            }
        }).print()
        ;
        //执行Job
        streamExecutionEnvironment.execute("windowTest");

    }
}
