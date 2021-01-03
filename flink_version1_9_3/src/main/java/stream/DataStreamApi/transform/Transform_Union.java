package stream.DataStreamApi.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

import java.util.Collections;
/***
 * union算子:和connect算子区别: union可以合并多个流. connect算子只能合并2个流.
 *
 * */
public class Transform_Union {
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
        // 按照 fale进行分流:'b'是一个流,'g'是一个流
        SplitStream<Person> splitStream=mapStream.split(new OutputSelector<Person>() {
            @Override
            public Iterable<String> select(Person person) {
                return person.getFale().equals("g")? Collections.singletonList("girl"):Collections.singletonList("boy");
            }
        });
        DataStream<Person> selectGirl=splitStream.select("girl");
        DataStream<Person> selectBoy=splitStream.select("boy");
        DataStream<Person> selectAll=splitStream.select("boy","girl");
        //使用union算子合流
        DataStream<Person> unionStream=selectGirl.union(selectBoy,selectAll);
        unionStream.print("union");
        streamExecutionEnvironment.execute("unionStream");
    }
}
