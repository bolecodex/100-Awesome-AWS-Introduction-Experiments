package com.myorg;

import java.util.Map;
import java.util.HashMap;

import software.constructs.Construct;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.TableEncryption;

import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Code;
public class HitCounter extends Construct{

    private final Table table;
    private final Function handler;

    public HitCounter(final Construct scope, final String id, final HitCounterProps props) throws RuntimeException {
        super(scope, id);

        if (props.getReadCapacity()!=null){
            if (props.getReadCapacity().intValue()<5 || props.getReadCapacity().intValue()>20){
                throw new RuntimeException("readCapacity must be greater than 5 or less than 20");
            }
        }
        Number readCapacity = (props.getReadCapacity()==null)?5:props.getReadCapacity();

        this.table = Table.Builder.create(this, "Hits")
        .encryption(TableEncryption.AWS_MANAGED)
        .partitionKey(Attribute.builder()
            .name("path")
            .type(AttributeType.STRING)
            .build())
        .readCapacity(readCapacity)
        .build();

        final Map<String, String> environment = new HashMap<>();
        
        environment.put("DOWNSTREAM_FUNCTION_NAME", props.getDownstream().getFunctionName());
        environment.put("HITS_TABLE_NAME", this.table.getTableName());
        
        this.handler = Function.Builder.create(this, "HitCounterHandler")
        .runtime(Runtime.NODEJS_14_X)
        .handler("hitcounter.handler")
        .code(Code.fromAsset("lambda"))
        .environment(environment)
        .build();

        // Number readCapacity;
        // if(props.getReadCapacity()==null){
        //     readCapacity = 5;
        // }else{
        //     readCapacity = props.getReadCapacity();
        // }



        this.table.grantReadWriteData(this.handler);

        props.getDownstream().grantInvoke(this.handler);

    }
    public Table getTable(){
        return this.table;
    }
    public Function getHandler(){
        return this.handler;
    }
}
