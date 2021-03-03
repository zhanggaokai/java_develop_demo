package stream.TableApi.UTIL_UDF;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.functions.ScalarFunction;

/**
 * 标量函数操作: 例如length函数，取字段的长度.
 * */
public class UDF_Function {
    /**GetLen:输出字段的长度*/
    public static class GetLen extends ScalarFunction{
        public GetLen(){};
        public int eval(String s){return s.length();}
    }

    public static void main(String[] args) {
        StreamExecutionEnvironment streamEnv=StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv=StreamTableEnvironment.create(streamEnv);
        DataStreamSource<String> file = streamEnv.readTextFile("D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\person.txt");
        
    }
}
