package stream.TableApi;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Csv;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.types.Row;
import stream.DataStreamApi.watermark.Person;

public class Example {
    /**
     * 对 Table APi 进行常规操作
     * */
    public void tableApiOpt() throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度为1
        streamExecutionEnvironment.setParallelism(1);
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
                .map(s->{
                    String[] str=s.split(",");
                    String name=str[0];
                    Integer id=Integer.parseInt(str[1]);
                    Long event_time=Long.parseLong(str[2]);
                    System.out.println("当前输入name:"+name+"id:"+id);
                    return new Person(name,id,event_time);
                });
        //创建表环境
        StreamTableEnvironment tableEnv=StreamTableEnvironment.create(streamExecutionEnvironment);
        //创建表
        Table dataTable=tableEnv.fromDataStream(dataStream);
        //Table Api 转换操作
        Table resultTable=dataTable.select("name,id").where("name='lisi'");
        //执行sql
        tableEnv.createTemporaryView("person",dataTable);
        String sql="select name,id from person where name='jack'";
        Table resultSqlTable=tableEnv.sqlQuery(sql);
        //打印输出
        tableEnv.toAppendStream(resultTable, Row.class).print("result");
        tableEnv.toAppendStream(resultSqlTable, Row.class).print("sql");
        streamExecutionEnvironment.execute();
    }
    /**
     * 批流环境配置 常规操作
     * */
    public void envOpt(){
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度为1
        streamExecutionEnvironment.setParallelism(1);
        //使用老的planner的流环境
        EnvironmentSettings oldStreamSettings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .useOldPlanner()
                .build();
        StreamTableEnvironment oldStreamTableEnv = StreamTableEnvironment.create(streamExecutionEnvironment, oldStreamSettings);
        //使用新的planner的流环境
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .useBlinkPlanner()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(streamExecutionEnvironment, blinkStreamSettings);

        //使用老的planner的批环境
        ExecutionEnvironment batchEnv=ExecutionEnvironment.getExecutionEnvironment();
        BatchTableEnvironment oldBatchTableEnv=BatchTableEnvironment.create(batchEnv);
        //使用新的planner的批环境
        EnvironmentSettings blinkBatchSettings = EnvironmentSettings.newInstance()
                .inBatchMode()
                .useBlinkPlanner()
                .build();
        TableEnvironment blinkBatchTableEnv = TableEnvironment.create(blinkBatchSettings);

    }
    /**
     * 表模式
     tableEnv
     .connect(...)  //定义表的数据来源,和外部系统建立连接
     .withFormat(...)  //定义数据格式化方法
     .withSchema(...)  //定义表结构
     .createTemporaryTable("MyTable");  //创建临时表
     *
     * 输入输出操作
     StreamTableEnvironment tableEnv=   //创建表的执行环境
     //source:创建一张表,用于读取数据
     tableEnv.connect(外部系统，例如Kafka).createTemporaryTable("inputTable");

     //transform:计算结果
     Table sqlResult=tableEnv.sqlQuery("SELECT ... From inputTable...);
     Table result=tableEnv.from("inputTable").select(...);


     //sink:将计算结果写入输出表中
     tableEnv.connect(外部系统例如mysql).createTemporaryTable("outputTable");
     result.insertInto("outputTable");

     * */
    // 操作文件系统
    public void tableSchemaOptFile() throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment=StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度为1
        streamExecutionEnvironment.setParallelism(1);
        //使用新的planner的流环境
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .useBlinkPlanner()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(streamExecutionEnvironment, blinkStreamSettings);
        //读取source文件
        String path="D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\person.txt";
        blinkStreamTableEnv.connect(new FileSystem().path(path))
        .withFormat(new Csv())
        .withSchema(new Schema().field("name", DataTypes.STRING())
        .field("age",DataTypes.INT())
        .field("sex",DataTypes.STRING())
        )
        .createTemporaryTable("inputTable");
        Table inputTable1=blinkStreamTableEnv.from("inputTable");
        // Table api常规操作------------------------
        Table tableFileter=inputTable1.select("sex,age").filter("sex === 'b'"); //过滤
        Table tableGroup=inputTable1.groupBy("sex")
                .select("sex,age.sum as sum_age,age.avg as avg_age");  //聚合操作

        //sql 操作
        Table tableSql=blinkStreamTableEnv.sqlQuery("select sex,age  from inputTable where sex='b'");

        //写入sink
        String sinkPath="D:\\work\\java\\flink_version1_9_3\\src\\main\\resources\\sink_person.txt";
        blinkStreamTableEnv.connect(new FileSystem().path(sinkPath))
                .withFormat(new Csv())
                .withSchema(new Schema().field("sex", DataTypes.STRING())
                        .field("age",DataTypes.INT())
                )
                .createTemporaryTable("OutputTable");
        tableSql.insertInto("OutputTable");
        //----------------------------------------
        inputTable1.printSchema(); //打印表模式
        Table resultTable=blinkStreamTableEnv.sqlQuery("select sex,sum(age) as age from inputTable group by sex");
        blinkStreamTableEnv.toAppendStream(inputTable1,Row.class).print("明细");
        blinkStreamTableEnv.toRetractStream(resultTable,Row.class).print("汇总");
        streamExecutionEnvironment.execute();
    }
    public static void main(String[] args) throws Exception {
        Example example=new Example();
        example.tableSchemaOptFile();
//        example.envOpt();
//        example.tableApiOpt();
    }
}
