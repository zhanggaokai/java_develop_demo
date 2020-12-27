package stream.DataStreamApi.Source;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**使用集合作为Source数据源*/
public class CollectionSource {
    public static void main(String[] args) throws Exception{
        //创建执行环境
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<SensorReading> dataStream=env.fromCollection(Arrays.asList(new SensorReading("sensor_6",1547718201L,15.4),
                new SensorReading("sensor_7",1547718202L,6.7),
                new SensorReading("sensor_10",1547718205L,38.1)
                ));
        dataStream.print("collection源");
        env.execute("collectionSource");

    }
}
//传感器温度读数的数据类型
class SensorReading{
    //属性:id,时间戳,温度值
    private String id;
    private Long timestamp;
    private Double temperature;

    public SensorReading() {
    }

    public SensorReading(String id, Long timestamp, Double temperature) {
        this.id = id;
        this.timestamp = timestamp;
        this.temperature = temperature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", temperature=" + temperature +
                '}';
    }
}
