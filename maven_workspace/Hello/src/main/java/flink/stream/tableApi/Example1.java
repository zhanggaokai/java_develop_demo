package flink.stream.tableApi;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.BatchTableEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.Types;
import org.apache.flink.table.descriptors.Csv;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.Schema;

/**
 * 类的目的: 1. how to create a table  </br>
 *          2.how to emit a table   </br>
 *          3.how to query a table  </br>
 * 流式处理举例,批量处理举例
 * */
public class Example1 {
    public static void main(String[] args) {

    }
    /**
     * 批量处理举例
     * */
    public static void batchTable(){
        ExecutionEnvironment env=ExecutionEnvironment.getExecutionEnvironment();
        BatchTableEnvironment tEnv=BatchTableEnvironment.getTableEnvironment(env);
        String path="D:\\tmp\\input\\2.txt";
        tEnv.connect(new FileSystem().path(path)).withFormat(new Csv().field("word", Types.STRING()).lineDelimiter("\n"))
                .withSchema(new Schema().field("word",Types.STRING()))
                .registerTableSource("fileSource");
        Table result=tEnv.scan("fileSource")
                .groupBy("word")
                .select("word,count(1) as count");
        result.printSchema();

    }
}
