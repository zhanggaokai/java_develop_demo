package flink.stream;


import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Types;
import org.apache.flink.table.api.java.StreamTableEnvironment;

import org.apache.flink.table.descriptors.Csv;
import org.apache.flink.table.descriptors.Kafka;

/**
 * 类 功能介绍: ods层数据存储在kafka,dwd层数据也存到kafka.使用flink-kafka-connector 来实现 table sql & kafka 的整合操作
 * 完成 实时流数仓中 ods层 test2 主题到  dwd层 click主题的 etl流程
 * */
public class Ods_To_Dwd {
    //构建 ods层 kafka流  --table 模式
    //解析csv
    public static void csvToTable(){
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        StreamTableEnvironment tableEnv=StreamTableEnvironment.getTableEnvironment(env);
        tableEnv.connect(new Kafka()
                .version("0.11")
                .topic("test2")
                .property("bootstrap.servers","localhost:9092")
                .property("zookeeper.connect", "localhost:2181")
                .property("group.id","g1")
                .startFromEarliest()
        ).withFormat(new Csv()
        .field("name",Types.STRING())
                .field("age",Types.INT())
                .field("currtime",Types.STRING())
                .fieldDelimiter(",").lineDelimiter("\n").quoteCharacter('"')
        ).inAppendMode().registerTableSourceAndSink("message");
        tableEnv.sqlQuery("select * from message").printSchema();
        tableEnv.execEnv();
    }
    public static void main(String[] args) {
        csvToTable();
    }
}
