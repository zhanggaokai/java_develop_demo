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


/**Connect算子:把2个流[connect算子只能合并2个流.所以又叫一国两制算子]合成一个流.配合 Map,FlatMap使用!
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
public class Transform_Connect {
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

        //使用connect算子合流
        ConnectedStreams<Person,Person> connectedStreams=selectGirl.connect(selectBoy);
        DataStream<Object> connectMapStream=connectedStreams.map(new CoMapFunction<Person, Person, Object>() {
            @Override
            public Object map1(Person person) throws Exception {
                return new Tuple2<String,Integer>(person.getName(),person.getAge());
            }

            @Override
            public Object map2(Person person) throws Exception {
                return new Tuple3<String,Integer,String>(person.getName(),person.getAge(),person.getFale());
            }
        });
        connectMapStream.print("connectStream");
        streamExecutionEnvironment.execute("connectStream");
    }
}
