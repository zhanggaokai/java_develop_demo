package kafka;
/**
 * 如何保证 消息有序消费?  答:把数据 只写到一个分区中。【或者把topic只设置一个分区】
 * 同一个消费者组 不会重复 消费 消息。
 * 不同的消费者组可以 消费 同一个消息
 * 消费消息的最小单位是partiton
 * producer 向 leader 中 写入消息.并同步给follower之后。 consumer 再进行消费。
 * 同一个消费者组 中的 消费者  在 同一时间 只能 由一个 消费者 消费 消息.
 * */
public class Kafka_Opt {
    public static void main(String[] args) {

    }

}

