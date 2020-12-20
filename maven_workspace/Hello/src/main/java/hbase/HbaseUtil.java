package hbase;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 对hbase 进行基本操作:
 * 获取Table
 * 保存单列数据
 * 查询单列数据
 * 保存多列数据
 * 查询多列数据
 * 删除数据
 * */
public class HbaseUtil {
    /**
     * 生成Configuration
     * */
    public static Configuration getConfiguration(){
        Configuration config= HBaseConfiguration.create();
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.zookeeper.quorum", "127.0.0.1");
        return config;
    }
    /**
     * 生成Connection
     * */
    public static Connection getConnection(Configuration configuration) throws IOException {
        Connection connection= ConnectionFactory.createConnection(configuration);
        return connection;
    }
    /**
     * 生成Admin
     * */
    public static Admin getAdmin(Connection connection) throws IOException {
        Admin admin=connection.getAdmin();
        return admin;
    }
    /**
     * 不强制性创建表
     * @tableName 表名
     * @family 列族列表
     * config 配置信息
     * */
    public static void createTable(String tableName,String[] family,Configuration config) throws Exception {
        Connection connection=ConnectionFactory.createConnection(config);
        Admin admin=connection.getAdmin(); //hbase api
        HTableDescriptor desc=new HTableDescriptor(TableName.valueOf(tableName));
        for(int i=0;i< family.length;i++)
        {
            desc.addFamily(new HColumnDescriptor(family[i]));
        }
        if(admin.tableExists(desc.getTableName())){
            System.out.println("table Exists!");
            //throw new Exception("table Exists!");
        }else{
            admin.createTable(desc);
            System.out.println("create table success!");
        }
    }
    /**
     * 强制性创建表
     * @tableName 表名
     * @family 列族列表
     * @config 配置信息
     * */
    public static void createTableForce(String tableName,String[] family,Configuration config) throws Exception{
        Connection connection=ConnectionFactory.createConnection(config);
        Admin admin= connection.getAdmin();
        HTableDescriptor desc=new HTableDescriptor(TableName.valueOf(tableName));
        for(int i=0;i< family.length;i++){
            desc.addFamily(new HColumnDescriptor(family[i]));
        }
        if(admin.tableExists(desc.getTableName())){
            admin.disableTable(desc.getTableName());
            admin.deleteTable(desc.getTableName());
        }
        admin.createTable(desc);
        System.out.println("create table "+tableName+" success!");
    }

    /**
     * 删除表
     * @tableName 表名
     * @config 配置信息
     * */
    public static void deleteTable(String tableName,Configuration config) throws Exception{
        Connection connection=ConnectionFactory.createConnection(config);
        Admin admin=connection.getAdmin();
        TableName tableName1=TableName.valueOf(tableName);
        if(admin.tableExists(tableName1)){
            admin.disableTable(tableName1);
            admin.deleteTable(tableName1);
            System.out.println(tableName+"已成功删除!");
        }

    }
    /**
 * 插入单行数据
 * @tableName 表名
 * @config 配置信息
 * @rowkey 行key
 * @colFamily 列族
 * @col 子列
 * @val 值
 */
    public static void insertRow(String tableName,Configuration config,String rowkey,String colFamily,String col,String val) throws Exception{
    Connection connection=ConnectionFactory.createConnection(config);
    Table table=connection.getTable(TableName.valueOf(tableName));
    Put put=new Put(Bytes.toBytes(rowkey));
    put.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes(col),Bytes.toBytes(val));
    table.put(put);
    table.close();
    System.out.printf("adding success!!Table:%s,Row:%s,Column=%s:%s,Value=%s\n", tableName, rowkey, colFamily, col, val);

}
    /**
     * 插入多行数据
     * @tableName 表名
     * @config 配置信息
     * @rowkey 行key
     * @colFamily 列族
     * @col 子列
     * @val 值
     */
    public static void insertMulRow(String tableName, Configuration config, List<Tuple4<String,String,String,String>> list) throws Exception{
        Connection connection=ConnectionFactory.createConnection(config);
        Table table=connection.getTable(TableName.valueOf(tableName));
        String rowkey=null;
        String colFamily=null;
        String col=null;
        String val=null;
        List<Put> puts=new ArrayList<Put>();
        for(Tuple4<String,String,String,String> str:list){
            rowkey=str.f0;
            colFamily= str.f1;;
            col=str.f2;
            val= str.f3;
            Put put=new Put(Bytes.toBytes(rowkey));
            put.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes(col),Bytes.toBytes(val));
            puts.add(put);
        }

        table.put(puts);
        table.close();
        System.out.println("insert multy rows success!");
        //System.out.printf("adding success!!Table:%s,Row:%s,Column=%s:%s,Value=%s\n", tableName, rowkey, colFamily, col, val);

    }
    /**
     * 删除数据
     * @tableName 表名
     * @config 配置信息
     * @rowkey 行key
     * @colFamily 列族
     * @col 子列
     * */
    public static void deleRow(String tableName,Configuration config,String rowkey,String colFamily,String col) throws Exception{

        Connection connection=ConnectionFactory.createConnection(config);
        Table table=connection.getTable(TableName.valueOf(tableName));
        Delete delete=new Delete(Bytes.toBytes(rowkey));
        //删除指定列族
        if(colFamily !=null ){
            delete.addFamily(Bytes.toBytes(colFamily));
        }
        //删除指定列
        if(colFamily !=null && col != null){
            delete.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes(col));
        }
        table.delete(delete);
        table.close();
        System.out.println("Row删除成功!");

    }
    public static void deleRow(String tableName, Configuration config, String rowkey, String colFamily) throws Exception {
        deleRow(tableName, config, rowkey, colFamily, null);
    }
    public static void deleRow(String tableName, Configuration config, String rowkey) throws Exception {
        deleRow(tableName, config, rowkey, null, null);
    }
    /*
     * 根据rowkey查找数据
     *
     * @tableName　表名
     * @config 配置信息
     * @rowkey 行key
     * @colFamily 列族
     * @col　子列
     *
     * */
    public static Result getData(String tableName, Configuration config, String rowkey, String colFamily, String col) throws Exception {
        Connection connection = ConnectionFactory.createConnection(config);
        Table table = connection.getTable(TableName.valueOf(tableName));
        Result result=null;
        try {
            Get get = new Get(Bytes.toBytes(rowkey));
            if (colFamily != null )
                get.addFamily(Bytes.toBytes(colFamily));
            if (colFamily != null && col != null)
                get.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(col));
           result = table.get(get);
            System.out.println("查询表:"+tableName);
            showCell(result); //展示查询结果
        }catch (Exception e){e.printStackTrace();}
        finally {
            table.close();
            return result;
        }
    }
    public static Result getData(String tableName, Configuration config, String rowkey, String colFamily) throws Exception {
        return getData(tableName, config, rowkey, colFamily, null);
    }

    public static Result getData(String tableName, Configuration config, String rowkey) throws Exception {
        return getData(tableName, config, rowkey, null, null);
    }


    /**
     * 批量查找数据
     * @table 表名
     * @config配置文件
     * @startRow 开始的行key
     * @stopRow　停止的行key
     *
     * hbase会将自己的元素按照key的ASCII码排序
     * 找出5193开头的元素
     *
     *   5193:1
     *   5193:2
     *   5194:1
     *   51939:1
     *   51942:1
     *
     *  scan.setStartRow("5193:#");
     *  scan.setStopRow("5193::");
     *
     *  原因：ASCII排序中："#" < "0-9" < ":"
     *  取出来的将是5193:后面跟着数字的元素
     * */
    public static List<Result> scanData(String tableName, Configuration config, String startRow, String stopRow, int limit) throws Exception {
        try (Connection connection = ConnectionFactory.createConnection(config)) {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            if (startRow != null && stopRow != null) {
                scan.setStartRow(Bytes.toBytes(startRow));
                scan.setStopRow(Bytes.toBytes(stopRow));
            }
            scan.setBatch(limit);
            List<Result> result = new ArrayList<Result>();
            ResultScanner resultScanner = table.getScanner(scan);
            for (Result r : resultScanner) {
                result.add(r);
            }
            table.close();
            return result;
        }
    }

    public static List<Result> scanData(String tableName, Configuration config, int limit) throws Exception {
        return scanData(tableName, config, null, null, limit);
    }

    /**
     * 打印表
     * @tables 打印的表描述对象
     *
     * */
    public static void printTables(HTableDescriptor[] tables) {
        for (HTableDescriptor t : tables) {
            HColumnDescriptor[] columns = t.getColumnFamilies();
            System.out.printf("tables:%s,columns-family:\n", t.getTableName());
            for (HColumnDescriptor column : columns) {
                System.out.printf("\t%s\n", column.getNameAsString());
            }
        }
    }

    /**
     * 格式化输出 查询结果
     * @result 结果
     *
     * */

    public static void showCell(Result result) {
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {

            System.out.println("RowKey:" + new String(CellUtil.cloneRow(cell)) + " ");
            System.out.println("Timetamp:" + cell.getTimestamp() + " ");
            System.out.println("column Family:" + new String(CellUtil.cloneFamily(cell)) + " ");
            System.out.println("col Name:" + new String(CellUtil.cloneQualifier(cell)) + " ");
            System.out.println("col value:" + new String(CellUtil.cloneValue(cell)) + " ");
            System.out.println("---------------");
        }
    }
    public static void main(String[] args) throws Exception {
//        String[] family={"impress_no","impress_time"};
////        createTable("mytable",family,getConfiguration()); //不强制创建hbase表
////        deleteTable("mytable",getConfiguration()); //删除表
////        insertRow("scores",getConfiguration(),"jack","course","english","100");
//        List<Tuple4<String,String,String,String>> list=new ArrayList<Tuple4<String,String,String,String>>();
//        list.add(new Tuple4<>("1001","course","english","20"));
//        list.add(new Tuple4<>("1002","course","english","20"));
//        insertMulRow("scores",getConfiguration(),list);
        getData("scores",getConfiguration(),"1001","course","english");

    }

}
