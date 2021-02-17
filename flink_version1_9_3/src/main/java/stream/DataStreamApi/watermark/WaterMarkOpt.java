package stream.DataStreamApi.watermark;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

/**对水位线进行操作*/
public class WaterMarkOpt {
    /**操作 时间语义：事件时间 的水位线
     **/
    public void eventWaterMarkOpt() throws Exception {

        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        //设置时间语义为事件时间
        streamExecutionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        streamExecutionEnvironment.setParallelism(1);
        //streamExecutionEnvironment.getConfig().setAutoWatermarkInterval(100);
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
        DataStream<Person> dataStream=streamExecutionEnvironment.socketTextStream("localhost",60000)
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
                })
                //设置Stream的watermark. 延迟为1秒. 以当前生成的系统时间作为事件时间
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Person>(Time.seconds(1)) {
                    @Override
                    public long extractTimestamp(Person person) {
                        return person.getEvent_time();
                    }
                });
        //设置5秒的时间窗口
        dataStream.keyBy("name").timeWindow(Time.seconds(5)).minBy("id").print("watermark");
        streamExecutionEnvironment.execute("watermark");
    }
    public static void main(String[] args) throws Exception {
        WaterMarkOpt event=new WaterMarkOpt();
        event.eventWaterMarkOpt();
    }
}
