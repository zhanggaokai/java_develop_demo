package stream.DataStreamApi.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Collections;

/**Split算子:把一个流通过 打标记 划分成多个流。配合Select算子进行分流操作!
 *
 * 数据源:persn.txt [name,age,fale]
 *zhangsan,24,b
 * wangwu,450,b
 * wangwu,45,b
 * zhangsan,100,b
 * lisi,30,g
 * chenzhen,56,g
 * chenzhen,560,g
 * lisi,300,g
 * */
public class Transform_Split {
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
        //打印分流
        selectGirl.print("girl");
        selectBoy.print("boy");
        selectAll.print("all");
        streamExecutionEnvironment.execute("SplitStream");
    }
}
