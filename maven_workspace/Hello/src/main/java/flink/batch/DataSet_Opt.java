package flink.batch;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * DataSet_Opt:介绍了 DataSet Api 常用算子操作: map,filter
 * */
public class DataSet_Opt {
    public static final ExecutionEnvironment ev=ExecutionEnvironment.getExecutionEnvironment();//创建DataSet上下文环境
    /**
     *dataSrource_Opt: 介绍了POJOs类型
     * */
    public static void dataSrource_Opt() throws Exception {
        Map<String,Integer> map1=new HashMap<>();
        map1.put("map1",10);map1.put("map2",20);
        DataSet<Map<String,Integer>> ds_map2=ev.fromElements(map1);
        ds_map2.flatMap(new FlatMapFunction<Map<String, Integer>, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(Map<String, Integer> stringIntegerMap, Collector<Tuple2<String, Integer>> collector) throws Exception {
                for(Map.Entry<String, Integer> entry:stringIntegerMap.entrySet())
                {
                    String key=entry.getKey();
                    Integer value=entry.getValue();
                    collector.collect(new Tuple2<>(key,value));
                }
            }
        }).groupBy(0).sum(1).print();
        DataSet<String> ds1= ev.fromElements("spark","flume","kafka","flume");
        DataSet<Tuple2<String,Integer>> ds2=ev.fromElements(new Tuple2<String,Integer>("flume",1),new Tuple2<String,Integer>("spark",2));
        DataSet<Person> pojos1=ev.fromElements(new Person("flume","10"),new Person("flink","20"));
        pojos1.map(new MapFunction<Person, Tuple2<String,String>>() {
            @Override
            public Tuple2<String, String> map(Person person) throws Exception {
                return new Tuple2<>(person.name, person.age);
            }
        }).print();
    }
/**
 * map_Opt : 描述了map算子的操作情况
 * */
    public static void map_Opt() throws Exception {
        DataSet<String> ds1= ev.fromElements("spark","flume","kafka","flume");
        DataSet<Tuple2<String,Integer>> map_Set1=ds1.map(new MapFunction<String,Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return new Tuple2<>(s,1);
            }
        });
        map_Set1.print();
    }
    /**
     * groupBy_Opt:描述了groupBy().sum()的用法。 实现wordcount操作
     * */
    public static void groupBy_Opt() throws Exception {
        DataSet<String> ds1=ev.fromElements("flume","flink","kafka","flink");
        ds1.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return new Tuple2<>(s,1);
            }
        }).groupBy(0).sum(1).print();
    }
    /**
     *flatMap_Opt: faltMap操作
     * */
    public static void flatMap_Opt() throws Exception {
        DataSet<String> ds1=ev.fromElements("flume flink","flink kafka","kafka","flink storm");
        DataSet<String> ds2=ds1.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                for(String str:s.split(" ")){
                    collector.collect(str);
                }
            }
        });
           ds2.map(new MapFunction<String, Tuple2<String,Integer>>() {
               @Override
               public Tuple2<String, Integer> map(String s) throws Exception {
                   return new Tuple2<>(s,1);
               }
           }).groupBy(0).sum(1).print();
    }
    /**
     * filter_Opt : 介绍了 filter算子操作
     * */
    public static void filter_Opt() throws Exception {
        DataSet<String> ds1=ev.fromElements("flume flink","flink kafka","kafka","flink storm");
        ds1.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String s) throws Exception {
                if(s.startsWith("flink")){
                    return true;
                }else {return false;}
            }
        }).print();
    }
    /**
     * writeAsText : 介绍了 writeAsText 方法
     * */
    public static void writeAsText_Opt() throws Exception {
        DataSet<String> ds1=ev.fromElements("flume flink","flink kafka","kafka","flink storm")
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String s, Collector<String> collector) throws Exception {
                        for(String str:s.split(" "))
                        {
                            collector.collect(str);
                        }
                    }
                });
        ds1.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return new Tuple2<>(s,1);
            }
        }).writeAsText("d://1.txt");
    }

    public static void main(String[] args) throws Exception {
//        DataSet_Opt.map_Opt();
        DataSet_Opt.dataSrource_Opt();
    }
}
class Person{
    public String name;
    public String age;
    public Person(){}
    public Person(String name,String age){this.name=name;this.age=age;}
}