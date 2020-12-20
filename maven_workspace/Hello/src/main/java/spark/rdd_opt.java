package spark;


import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.storage.StorageLevel;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * description:rdd算子操作
 *map算子操作
 * */
public class rdd_opt {
    public static void map_rdd_opt(JavaSparkContext sc){
        /*map rdd操作*/
        sc.setLogLevel("ERROR"); //设置打印日志级别
        String[] arr={"flume","flink","spark","flink"};
        List<String> l1= Arrays.asList(arr);
        JavaRDD<String>  rdd1=sc.parallelize(l1);
        JavaPairRDD<String,Integer> map_rdd =rdd1.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                Tuple2<String,Integer> t1=new Tuple2<>(s,1);
                return t1;
            }
        });
        System.out.println(map_rdd.collect());
    }
    public static void flatmap_rdd_opt(JavaSparkContext sc){
        JavaRDD<String> jr=sc.textFile("D:\\work\\data\\1.txt");
        JavaRDD<String> flatmap_rdd=jr.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                String[] str=s.split(",");
                List<String> l1=Arrays.asList(str);
                Iterator<String> it=l1.iterator();
                return it;
            }
        });
        System.out.println(flatmap_rdd.collect());
        JavaPairRDD<String,Integer> map_pair_rdd=flatmap_rdd.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                Tuple2<String, Integer> tp=new Tuple2<>(s,1);
                return tp;
            }
        });
        System.out.println(map_pair_rdd.collect());
        JavaPairRDD<String,Integer> reducebykey_rdd=map_pair_rdd.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer+integer2;
            }
        });
        System.out.println("reduceByKey:"+reducebykey_rdd.collect());
        JavaPairRDD<String,Integer> filter_rdd=reducebykey_rdd.filter(new Function<Tuple2<String, Integer>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                if(stringIntegerTuple2._2 >3){return true;}
                else {return false;}
            }
        });
        System.out.println("filter rdd算子操作:"+filter_rdd.collect());
        JavaPairRDD<String,Integer> sortbykey_rdd=reducebykey_rdd.sortByKey(false);
        /*持久化*/
        sortbykey_rdd.persist(StorageLevel.MEMORY_ONLY());
        System.out.println("sortbykey算子降序:"+sortbykey_rdd.collect());

    }
    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir","D:\\soft\\hadoop-2.7.6");
        SparkConf conf=new SparkConf();
        conf.setMaster("local").setAppName("map_rdd_test");
        JavaSparkContext sc = new JavaSparkContext(conf);
        //map_rdd_opt(sc);
        flatmap_rdd_opt(sc);
        sc.close();
    }
}
