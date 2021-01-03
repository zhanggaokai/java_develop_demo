package stream.DataStreamApi.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
/**
 * max算子与maxBy算子的区别?
 例如 group by fale, max(age)   ; max算子只更新fale,age 这些汇总列。 而maxBy算子 会更新所有列
 * 输入:
 zhangsan,24,b
 wangwu,450,b
 wangwu,45,b
 zhangsan,100,b
 lisi,30,g
 chenzhen,56,g
 chenzhen,560,g
 lisi,300,g
 * */
public class Transform_maxBy {
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
        KeyedStream<Person, Tuple> keyedStream=mapStream.keyBy("fale","name"); //按照fale,name作为联合key
        DataStream<Person> sumStream=keyedStream.maxBy("age");// max只更新汇总列,maxBy会更新所有列
        sumStream.print();
        streamExecutionEnvironment.execute("keyedStream");
    }
}
