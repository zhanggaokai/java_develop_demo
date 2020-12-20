package batch.tableapi;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.types.Row;

/**
 * 创建 udf 函数类
 * */
public class MyMapFunction extends ScalarFunction {
    public String eval(String a) {
        return ("pre-" + a);
    }

    @Override
    public TypeInformation<?> getResultType(Class<?>[] signature) {
        return Types.STRING;
    }
}