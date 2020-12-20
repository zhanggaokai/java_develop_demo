package spark;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * description:dataframe操作. dataframe和hive交互
 * */
public class dataframe_opt {
    public static void dataframe(SparkSession spark){
        //读取文本,以|作为分隔符
        Dataset<Row> ds=spark.read().option("delimiter","|").csv("D:\\work\\data\\2.txt");
        //给列重命名,修改类型
        ds=ds.withColumnRenamed("_c0","name")
                .withColumnRenamed("_c1","sex")
                .withColumnRenamed("_c2","age")
        ;
        ds=ds.withColumn("age", ds.col("age").cast(DataTypes.IntegerType));
        Dataset<Row>  dataset=ds.groupBy("sex").sum("age");
        List<Row> l1=new ArrayList<>();
        //把dataframe 通过rdd.toJavaRDD转换成JavaRDD;rdd通过collect转换为java 集合
        l1=dataset.rdd().toJavaRDD().collect();
        Iterator<Row> it=l1.iterator();
        while(it.hasNext()){
            Row r1=it.next();
            System.out.println("sex:"+r1.get(0)+"|age:"+r1.get(1));
        }
        dataset.createOrReplaceTempView("stu"); //注册成表
        System.out.println("dataframe注册成table:");
        spark.sql("select * from stu").show();

    }

    public static void main(String[] args) {
        SparkSession spark=SparkSession.builder()
                .appName("dataframe操作")
                .master("local")
                .enableHiveSupport()
                .getOrCreate();
        SparkContext sc=spark.sparkContext();
        sc.setLogLevel("ERROR");
        dataframe(spark);
        spark.close();
    }
}
