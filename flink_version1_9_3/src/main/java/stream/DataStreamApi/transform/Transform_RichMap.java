package stream.DataStreamApi.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
/**Rich算子可以调用底层的一些信息:例如state状体.*/
public class Transform_RichMap {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        streamExecutionEnvironment.setParallelism(4);
        String path="D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\Word.txt";
        DataStream<String> inputStream=streamExecutionEnvironment.readTextFile(path);
        DataStream<Tuple2<String,Integer>> mapStream=inputStream.map(new MyRichMap());
        mapStream.print();
        streamExecutionEnvironment.execute("map_operation");
    }
    public static class MyRichMap extends RichMapFunction<String, Tuple2<String,Integer>> {
        @Override
        public void open(Configuration parameters) throws Exception {
            System.out.println("open");
        }

        @Override
        public Tuple2<String, Integer> map(String s) throws Exception {
            //getRuntimeContext().getState();
            return new Tuple2<String, Integer>(s,1);
        }

        @Override
        public void close() throws Exception {
            System.out.println("close");
        }
    }
}
