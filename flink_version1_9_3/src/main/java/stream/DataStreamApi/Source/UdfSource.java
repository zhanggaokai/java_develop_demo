package stream.DataStreamApi.Source;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

/**自定义外部数据源
 * 自定义外部数据源 需要 实现 SourceFunction接口的run方法
 * */
public class UdfSource {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Person> dataStream=streamExecutionEnvironment.addSource(new MyudfSource());
        dataStream.flatMap(new FlatMapFunction<Person, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(Person person, Collector<Tuple2<String, Integer>> collector) throws Exception {
                collector.collect(new Tuple2<>(person.getName(),1));
                collector.collect(new Tuple2<>(person.getSex(),1));
            }
        }).keyBy(0).sum(1).print();
        /**execute 方法 启动流 任务 */
        streamExecutionEnvironment.execute("wordcount");
    }

}
//发送出去的数据类型 是PoJo类  SensorReading
class MyudfSource implements SourceFunction<Person>{
    //定义一个标识位
    private boolean running=true;
    @Override
    public void run(SourceContext<Person> sourceContext) throws Exception {
        while (running){
            sourceContext.collect(new Person("zhang","g"));
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {
        running=false;
    }
}
class Person{
    private String name;

    public Person(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}