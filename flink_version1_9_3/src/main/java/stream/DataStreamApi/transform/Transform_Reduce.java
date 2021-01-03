package stream.DataStreamApi.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
/**reduce算子:聚合操作. 需要先进行keyBy分流*/
public class Transform_Reduce {
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
        KeyedStream<Person, Tuple> keyedStream=mapStream.keyBy("fale"); //按照fale分流
        DataStream<Person> sumStream=keyedStream.reduce(new ReduceFunction<Person>() {
            @Override
            public Person reduce(Person person, Person t1) throws Exception {
                // t1是当前传入的数据; person是之前聚合的状态数据
                return new Person(person.getName(), Math.max(person.getAge(), t1.getAge()),person.getFale());
            }
        });
        sumStream.print();
        streamExecutionEnvironment.execute("ReduceStream");
    }
}
