package flink.stream;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

/**
 * 设置检查点的好处:当flink崩溃时,可以根据检查点 恢复。而不需要从0开始计算.
 * 状态存储方式分 内存，文件系统，RockDb三种，如下：
    MemoryStateBackend 默认，小状态，本地调试使用 [放内存容易down掉flink集群]
    FsStateBackend 大状态，长窗口，高可用场景
   RocksDBStateBackend 超大状态，长窗口，高可用场景，可增量checkpoint
 *
 * */
public class CheckPoint_Opt {
    private static StreamExecutionEnvironment ev=StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args) throws Exception {
        DataStream<String> ds1=ev.fromElements("spark","flink","kafka");
        ds1.print();

        ev.enableCheckpointing(5000);//5秒启动一次checkpoint
        ev.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE); //只checkpoint一次
        ev.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000); //设置两次checkpoint的最小时间间隔
        ev.getCheckpointConfig().setCheckpointTimeout(60000); //设置checkpoint超时时长 60秒
        ev.getCheckpointConfig().setMaxConcurrentCheckpoints(1); //设置并行度1
        //程序关闭时 出发额外的checkpoint
        ev.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        ev.setStateBackend(new MemoryStateBackend(10240,false)); //设置checkpoint存储路径到内存，大小10kb
        FlinkKafkaConsumer011<String> kafka =new FlinkKafkaConsumer011<String>("test2",new SimpleStringSchema(),CheckPoint_Opt.kafkaConsumerProperties());
        //返回kafkaSource<String>
        DataStreamSource<String> dsSource=ev.addSource(kafka);

        ev.execute("checkPoint_Test");

    }
    public static Properties kafkaConsumerProperties(){
        //Kafka props
        Properties properties = new Properties();
        //指定Kafka的Broker地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //指定组ID
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "g1");
        //如果没有记录偏移量，第一次从最开始消费
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }
}
