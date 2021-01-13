package stream.DataStreamApi.transform;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**flatMap算子操作: 1行输入,多行输出*/
public class Transform_FlatMap {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        String path="D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\FlatMap.txt";
        DataStream<String> inputStream=streamExecutionEnvironment.readTextFile(path);
        DataStream<Tuple2<String,Integer>> flatMapStream=inputStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                for(String str:s.split(",")){
                    collector.collect(new Tuple2<>(str,1));
                }
            }
        });

        flatMapStream.print();
        streamExecutionEnvironment.execute("flatmap_operation");
    }
}
