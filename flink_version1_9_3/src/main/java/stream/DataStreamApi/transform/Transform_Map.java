package stream.DataStreamApi.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**map算子操作: 输入1行,输出一行*/
public class Transform_Map {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        String path="D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\Word.txt";
        DataStream<String> inputStream=streamExecutionEnvironment.readTextFile(path);
        DataStream<Tuple2<String,Integer>> mapStream=inputStream.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return new Tuple2<String, Integer>(s,1);
            }
        });
        mapStream.print();
        streamExecutionEnvironment.execute("map_operation");
    }
}
