package flink.stream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.util.Collector;
/**
 * 数据源: connect 算子,
 * */
public class DataStream_Opt {
    public static final StreamExecutionEnvironment streamEv=StreamExecutionEnvironment.getExecutionEnvironment();//创建流上下文
   /**
    * connect 算子 操作：把两个不同的DataStream合并为一个DataStream
    * */
    public static void connect_Opt(){
        DataStream<String> ds1=streamEv.readTextFile("d:\\tmp\\input\\1.txt");
        DataStream<String> ds2=streamEv.readTextFile("d:\\tmp\\input\\2.txt");
        ConnectedStreams<String,String> connect1=ds1.connect(ds2);
        connect1.map(new CoMapFunction<String, String, Tuple2<String,String>>() {
            @Override
            public Tuple2<String, String> map1(String s) throws Exception {
                return new Tuple2<>(s,"1");
            }

            @Override
            public Tuple2<String, String> map2(String s) throws Exception {
                return new Tuple2<>(s,"1");
            }
        }).print();
   }

    public static void main(String[] args) throws Exception {
        //connect_Opt();
        streamEv.execute();
    }
}
