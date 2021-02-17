package stream.ProcessFunctionApi;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import stream.DataStreamApi.watermark.Person;
/**如果*/
public class KeyedProcessFunction_Test {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);//设置并行度
        //接受socket 数据源
        /**
         * 输入数据:name,id,event_time
         jack,3,1613113861000      2021-02-12 15:11:01
         jack,1,1613113862000      2021-02-12 15:11:02    [窗口1]

         jack,2,1613113865000      2021-02-12 15:11:05
         lisi,13,1613113866000     2021-02-12 15:11:06    [窗口2]触发

         lisi,10,1613113870000     2021-02-12 15:11:10
         lisi,16,1613113871000     2021-02-12 15:11:11    [窗口3]触发
         *
         打开nc 端
         cd  D:\soft\nc   nc -l -p 60000
         * */
        DataStream<Person> dataStream=env.socketTextStream("localhost",60000)
                .map(new MapFunction<String, Person>() {
                    @Override
                    public Person map(String s) throws Exception {
                        String[] str=s.split(",");
                        String name=str[0];
                        Integer id=Integer.parseInt(str[1]);
                        Long event_time=Long.parseLong(str[2]);
                        System.out.println("当前输入name:"+name+"id:"+id);
                        return new Person(name,id,event_time);
                    }
                });
        dataStream.keyBy("name").process(new MyProcess()).print();
        env.execute("KeyedProcessFunction_Test");

    }
    public static class MyProcess extends KeyedProcessFunction<Tuple, Person,Integer> {
        public MyProcess(){};
        @Override
        public void processElement(Person person, Context context, Collector<Integer> collector) throws Exception {
            collector.collect(person.getId());
        }
    }
}
