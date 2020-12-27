package stream.DataStreamApi;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 *对 流 进行 数据进行 单词统计 处理! 流处理和批处理的区别在于: 流处理 对每一个输入 都会记录 状态的变化
 2> (azkaban,1)
 1> (spark,1)
 这里的 2> 1>表示多线程的并行度
 * */
public class StreamWordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        streamExecutionEnvironment.setParallelism(4); //设置并行度
        streamExecutionEnvironment.disableOperatorChaining(); //不进行任务链合并操作
        //String path="D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\Word.txt";
        // //读取文本数据源
        //DataStream<String> dataStream=streamExecutionEnvironment.readTextFile(path);
        //接受socket 数据源
        //使用 ParameterTool 提取参数 配置项
        ParameterTool parameterTool=ParameterTool.fromArgs(args); //获取参数
        String hostname=parameterTool.get("hostname");
        int port=Integer.parseInt(parameterTool.get("port"));
        DataStream<String> dataStream=streamExecutionEnvironment.socketTextStream(hostname,port);
        dataStream.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                for(String str:s.split(",")){
                    collector.collect(new Tuple2<>(str,1));
                }
            }
        }).keyBy(0).sum(1).print();
        /**execute 方法 启动流 任务 */
        streamExecutionEnvironment.execute("wordcount");
    }
}
