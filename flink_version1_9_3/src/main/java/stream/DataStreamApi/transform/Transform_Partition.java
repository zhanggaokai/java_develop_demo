package stream.DataStreamApi.transform;
/**
 * 重分区算子：把上游数据 按不同的分区方式 发送给下游的不同分区
 * 算子: rescale 按组分发 ;rebalance 轮询分发 ;global 只分发到一个分区 ;partition 按照key 分发
 * */
public class Transform_Partition {
}
