package com.myorg;

import java.util.HashMap;
import java.util.Map;

import software.constructs.Construct;

import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableEncryption;

import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;

// HitCounter是我们构建的自定义Construct
public class HitCounter extends Construct {
    // 声明HitCounter的内部属性
    private final Function handler;
    private final Table table;

    /**
     * HitCounter的构造函数，通过将参数传递给Construct基类，
     * @param scope Construct类型
     * @param id 
     * @param props
     */
    // RuntimeException is the superclass of those exceptions that can be thrown during the normal operation of the Java Virtual Machine.
    public HitCounter(final Construct scope, final String id, final HitCounterProps props) throws RuntimeException {
        // 调用父类Construct的构造函数
        super(scope, id);

        if (props.getReadCapacity() != null) {
            if (props.getReadCapacity().intValue() < 5 || props.getReadCapacity().intValue() > 20) {
                throw new RuntimeException("readCapacity must be greater than 5 or less than 20");
            }
        }

        // Number readCapacity;
        // if(props.getReadCapacity() == null){
        //     readCapacity = 5;
        // }else{
        //     readCapacity = props.getReadCapacity();
        // }
        // 三目运算符，等价于上面的注释
        Number readCapacity = (props.getReadCapacity() == null) ? 5 : props.getReadCapacity();

        // 使用Table内部自定义的Builder来创建DynamoDB Table实例，path作为分区键
        this.table = Table.Builder.create(this, "Hits")
            .partitionKey(Attribute.builder()
                .name("path")
                .type(AttributeType.STRING)
                .build())
            .encryption(TableEncryption.AWS_MANAGED)
            .readCapacity(readCapacity)
            .build();
        
        // 为创建上面的handler准备环境变量, Map<String, String>为泛型，在这里将数据类型String作为参数传进去
        final Map<String, String> environment = new HashMap<>();
        environment.put("DOWNSTREAM_FUNCTION_NAME", props.getDownstream().getFunctionName());
        environment.put("HITS_TABLE_NAME", this.table.getTableName());

        // 创建Lambda函数HitCounterHandler
        // DOWNSTREAM_FUNCTION_NAME和HITS_TABLE_NAME通过环境变量后期绑定，在实际stack进行deploy时才被绑定值
        this.handler = Function.Builder.create(this, "HitCounterHandler")
            .runtime(Runtime.NODEJS_14_X)
            .handler("hitcounter.handler")
            .code(Code.fromAsset("lambda"))
            .environment(environment)
            .build();

        // Grants the lambda function read/write permissions to our table
        this.table.grantReadWriteData(this.handler);
        
        // Grants the lambda function invoke permissions to the downstream function
        props.getDownstream().grantInvoke(this.handler);
    }

    // 提供获取handler和table的方法
    /**
     * @return the counter definition
     */
    public Function getHandler() {
        return this.handler;
    }

    /**
     * @return the counter table
     */
    public Table getTable() {
        return this.table;
    }
}