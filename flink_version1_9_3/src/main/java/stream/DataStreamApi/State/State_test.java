package stream.DataStreamApi.State;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.executiongraph.restart.RestartStrategy;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import stream.DataStreamApi.watermark.Person;
/** 设定检查点*/
public class State_test {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        //设置检查点信息
        streamExecutionEnvironment.enableCheckpointing(100); //设定100毫秒生成一下检查点信息.
        //高级设置
        streamExecutionEnvironment.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        streamExecutionEnvironment.getCheckpointConfig().setCheckpointTimeout(60000); //设置超时时间1分钟
        streamExecutionEnvironment.getCheckpointConfig().setMaxConcurrentCheckpoints(2); //设置最大同时可以保存的检查点为2个。
        streamExecutionEnvironment.getCheckpointConfig().setTolerableCheckpointFailureNumber(0);//允许检查点保存失败的次数.
        //延迟重启策略
        streamExecutionEnvironment.setRestartStrategy(RestartStrategies.fixedDelayRestart(3,10000));//每10秒尝试重启一次,尝试3次
        //失败率重启  每1分钟重启一次，重启时间超过10分钟算重启失败.重启3次
        streamExecutionEnvironment.setRestartStrategy(RestartStrategies.failureRateRestart(3, Time.minutes(10),Time.minutes(1)));
        DataStream<String> dataStream=streamExecutionEnvironment.socketTextStream("localhost",60000);

        streamExecutionEnvironment.execute("state");

    }
}
