package Batch.DataSetApi;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
/**
 * DataSet Api 实现 WordCount 单词统计
 * */
public class WordCount {
    public static void main(String[] args) throws Exception {
        //创建批处理 执行环境
        ExecutionEnvironment executionEnvironment=ExecutionEnvironment.getExecutionEnvironment();
        String path="D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\Word.txt";
        //读取文本文件
        DataSet<String> source=executionEnvironment.readTextFile(path);
        source.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                for(String str:s.split(",")){
                    collector.collect(new Tuple2<>(str,1));
                }
            }
        }).groupBy(0).sum(1).print();
    }
}
