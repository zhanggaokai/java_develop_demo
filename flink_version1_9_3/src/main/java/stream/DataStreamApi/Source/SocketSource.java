package stream.DataStreamApi.Source;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
/**socket作为数据源
 * nc -l -p 60000  启动socket服务器 [cd D:\soft\nc\]
 * */
public class SocketSource {

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
