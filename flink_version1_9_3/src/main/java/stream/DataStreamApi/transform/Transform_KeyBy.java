package stream.DataStreamApi.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/**keyBy:不是一个算子.把数据生成hash值,然后进行mod取模。分发给不同的分区，再配合聚合算子进行计算*/
public class Transform_KeyBy {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        streamExecutionEnvironment.setParallelism(1);
        String path="D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\person.txt";
        DataStream<String> inputStream=streamExecutionEnvironment.readTextFile(path);
        DataStream<Person> mapStream=inputStream.map(new MapFunction<String, Person>() {
            @Override
            public Person map(String s) throws Exception {
                String[] str=s.split(",");
                return new Person(str[0],Integer.parseInt(str[1]),str[2]);
            }
        });
        KeyedStream<Person,Tuple> keyedStream=mapStream.keyBy("fale","name"); //按照fale,name作为联合key
        DataStream<Person> sumStream=keyedStream.maxBy("age");// max 只更新age汇总列. maxBy全部更新
        sumStream.print();
        streamExecutionEnvironment.execute("keyedStream");
    }
}

