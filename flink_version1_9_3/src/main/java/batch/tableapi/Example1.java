package batch.tableapi;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.OldCsv;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.table.expressions.Expression;
import org.apache.flink.table.expressions.ExpressionVisitor;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.table.sinks.CsvTableSink;
import org.apache.flink.table.sinks.TableSink;
import org.apache.flink.table.sources.CsvTableSource;
import org.apache.flink.table.sources.TableSource;
import org.apache.flink.types.Row;

import java.util.List;

import static org.apache.calcite.linq4j.tree.Expressions.call;

/**
 * 类的作用: (1)介绍注册 表的方式; (2)对列进行添加删除操作; (3)如何实现table 列的 udf函数
 * 1：tableDescriptor 注册表
 * 2.使用 TableSource/TableSink 注册表
 * 3.使用 stream流 注册为 表
 * */
public class Example1 {
    private static ExecutionEnvironment env=ExecutionEnvironment.getExecutionEnvironment();
    private static BatchTableEnvironment tEnv=BatchTableEnvironment.create(env);
    private static StreamExecutionEnvironment streamEnv=StreamExecutionEnvironment.getExecutionEnvironment();
    private static StreamTableEnvironment streamTableEnv=StreamTableEnvironment.create(streamEnv);
    public static void main(String[] args) throws Exception {
        tableUdfMap();

    }
    /**
     * 使用connect连接器
     * 把file注册为table。
     * 指定分隔符相关操作
     * 把Table对象 转换为DataSet 把结果打印出来
     * 对列进行 添加删除操作
     * */
    public static void tableDescriptor() throws Exception {
        String path="D:\\tmp\\input\\2.txt";
        tEnv.connect(new FileSystem().path(path))
                .withFormat(new OldCsv().field("word",Types.STRING).lineDelimiter("\n"))
                .withSchema(new Schema().field("word",Types.STRING)) //定义字段schema
                .registerTableSource("flinksource"); //注册为表
        Table table=tEnv.scan("flinksource")
                .groupBy("word")
                .select("word,count(1) as cnt");
        Table table1=table.dropColumns("cnt"); // 删除 cnt 列
        Table table2=table.renameColumns("word as word1,cnt as cnum"); //更改表名
        Table table3=table.select("withColumns(1,2 to 2)");  //只选择第一列，第二列
        Table table4=table.select("withoutColumns(1 to 1)");  //不选择第一列
        Table map_table;
        tEnv.toDataSet(table3, Row.class).print();
    }
    /**
     * 对table中的列 进行 udf函数操作
     * */
    public static void tableUdfMap() throws Exception {
        String path="D:\\tmp\\input\\2.txt";
        tEnv.connect(new FileSystem().path(path))
                .withFormat(new OldCsv().field("word",Types.STRING).lineDelimiter("\n"))
                .withSchema(new Schema().field("word",Types.STRING)) //定义字段schema
                .registerTableSource("flinksource"); //注册为表
        Table table=tEnv.scan("flinksource")
                .groupBy("word")
                .select("word,count(1) as cnt");
        ScalarFunction func = new MyMapFunction();
        tEnv.registerFunction("func", func);
        Table maptable =table.select("word,cnt,func(word) as udf"); //udf函数的应用
        tEnv.toDataSet(maptable, Row.class).print();
    }
    public static void tableDescriptor1() throws Exception {
        String path="D:\\tmp\\input\\2.txt";
        tEnv.connect(new FileSystem().path(path))
                .withFormat(new OldCsv().field("word",Types.STRING).lineDelimiter("\n"))
                .withSchema(new Schema().field("word",Types.STRING)) //定义字段schema
                .registerTableSource("flinksource"); //注册为表
        Table table=tEnv.scan("flinksource")
                .select("word");
        tEnv.toDataSet(table, Row.class).print();

        String path1="D:\\tmp\\input\\3.txt";
        tEnv.connect(new FileSystem().path(path1))
                .withFormat(new OldCsv().field("word",Types.STRING).fieldDelimiter(","))
                .withSchema(new Schema().field("word",Types.STRING)) //定义字段schema
                .registerTableSink("targetTable"); //注册为表
        table.insertInto("targetTable");
    }
    /**
     * 使用TableSource  注册一个 table
     * */
    public static void tableSource() throws Exception {
        String path="D:\\tmp\\input\\2.txt";
        TableSource csvSource=new CsvTableSource(path,new String[]{"word"},new TypeInformation[]{Types.STRING});
       tEnv.registerTableSource("flinksource",csvSource);  //注册为表
        Table table=tEnv.scan("flinksource")
                .groupBy("word")
                .select("word,count(1) as cnt");
        tEnv.toDataSet(table, Row.class).print();
    }
    public static void tableSink() throws Exception {
        String path1="D:\\tmp\\input\\2.txt";
        TableSource csvSource=new CsvTableSource(path1,new String[]{"word"},new TypeInformation[]{Types.STRING});
        tEnv.registerTableSource("flinksource",csvSource);  //注册为表
        Table table=tEnv.scan("flinksource")
                .groupBy("word")
                .select("word");
        tEnv.toDataSet(table, Row.class).print();

        // tableSink
        String path="D:\\tmp\\input\\3.txt";
        TableSink csvSink=new CsvTableSink(path,",").configure(new String[]{"word"},new TypeInformation[]{Types.STRING});
        tEnv.registerTableSink("targetTable",csvSink);  //注册为表
        table.insertInto("targetTable");
    }
    public static void stream() throws Exception {

        String path="D:\\tmp\\input\\2.txt";
        DataStream<String> ds=streamEnv.readTextFile(path);
        streamTableEnv.registerDataStream("flinksource",ds,"word");
        Table table=streamTableEnv.scan("flinksource")
                .groupBy("word")
                .select("word,count(1) as cnt");
        streamTableEnv.toRetractStream(table, Row.class).print();
        streamTableEnv.execute("wordcount");
    }
}
