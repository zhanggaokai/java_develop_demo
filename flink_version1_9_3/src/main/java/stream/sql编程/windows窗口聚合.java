package stream.sql编程;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;

/**
 * 在 flink sql 中使用窗口
 * 窗口： 滚动窗口,滑动窗口,会话窗口
 * 窗口时间类型：事件时间,接入时间,处理时间
 tumble(t,INTERVAL '2' HOUR)   //滚动窗口: t:时间,2 表示窗口大小
 hop(t,INTERVAL '2' HOUR,INTERVAL '1' HOUR) //滑动窗口: t:时间,2 表示窗口大小,1表示滑动间隔
 session(t,INTERVAL '30' MINUTE)   //会话窗口: t:时间,30 表示窗口大小

 1.对于clicks表(user,cTime,url): 统计每个小时每个用户点击的次数
 user|cTime,url
 mary|12:00:00|baidu.com
 jone|12:03:10|google.com

 统计sql如下:
 select user,TUMBLE_END(cTime,INTERVAL '1' HOUR) as endT,count(url) as cnt
 from clicks group by
 (user,TUMBLE_END(cTime,INTERVAL '1' HOUR))
*  如何配置 flink table 动态表 ?
 * */
public class windows窗口聚合 {
    //创建blink batch
    private static EnvironmentSettings blinkBatchSettings=EnvironmentSettings.newInstance().useBlinkPlanner().inBatchMode().build();
    private  static TableEnvironment tEnvBlinkBatch=TableEnvironment.create(blinkBatchSettings);
    //创建 blink Stream
    private static EnvironmentSettings blinkStreamSettings=EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
    private static StreamExecutionEnvironment blinkStreamExeEnv=StreamExecutionEnvironment.getExecutionEnvironment();
    private static StreamTableEnvironment tEnvStreamBatch=StreamTableEnvironment.create(blinkStreamExeEnv,blinkStreamSettings);
    //创建 flink batch
    private static ExecutionEnvironment execenv=ExecutionEnvironment.getExecutionEnvironment();
    private  static BatchTableEnvironment tEnvflinkBatch=BatchTableEnvironment.create(execenv);
    //创建flink stream
    private static EnvironmentSettings flinkStreamSettings=EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build();
    private static TableEnvironment flinkStreamEnv=TableEnvironment.create(flinkStreamSettings);
    public static void main(String[] args) {
    }
}
