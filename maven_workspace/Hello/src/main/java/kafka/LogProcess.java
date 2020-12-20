package kafka;
/**
 *  去除 脏数据 操作!  Kafka Stream 操作.
 * */
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

public class LogProcess implements Processor<byte[],byte[]> {
    @Override
    public void init(ProcessorContext processorContext) {

    }

    @Override
    public void process(byte[] bytes, byte[] bytes2) {

    }

    @Override
    public void punctuate(long l) {

    }

    @Override
    public void close() {

    }
}
