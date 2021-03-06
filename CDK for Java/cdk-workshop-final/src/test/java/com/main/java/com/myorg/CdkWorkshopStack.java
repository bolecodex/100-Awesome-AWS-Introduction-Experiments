package com.myorg;

import io.github.cdklabs.dynamotableviewer.TableViewer;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.CfnOutput;

import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;

public class CdkWorkshopStack extends Stack {

    public final CfnOutput hcViewerUrl;
    public final CfnOutput hcEndpoint;
    
    public CdkWorkshopStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public CdkWorkshopStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        // Defines a new lambda resource
        // 链式调用，Builder是Function的内部类
        final Function hello = Function.Builder.create(this, "HelloHandler")
            .runtime(Runtime.NODEJS_14_X)    // execution environment
            .code(Code.fromAsset("lambda"))  // code loaded from the "lambda" directory
            .handler("hello.handler")        // file is "hello", function is "handler"
            .build();
        
        // System.out.println(hello.toString());

        // 定义hitcounter资源
        final HitCounter helloWithCounter = new HitCounter(this, "HelloHitCounter", HitCounterProps.builder()
            .downstream(hello)
            .build());

        // Defines an API Gateway REST API resource backed by our "hello" function
        // https://docs.aws.amazon.com/cdk/api/v1/docs/@aws-cdk_aws-apigateway.LambdaRestApi.html
        final LambdaRestApi gateway = LambdaRestApi.Builder.create(this, "Endpoint")
            .handler(helloWithCounter.getHandler())
            .build();

        // Defines a viewer for the HitCounts table
        // 定义DynamoDB的TableViewer，用来显示Table内容
        final TableViewer tv = TableViewer.Builder.create(this, "ViewerHitCount")
            .title("Hello Hits")
            .table(helloWithCounter.getTable())
            .sortBy("-hits")
            .build();

        
        hcViewerUrl = CfnOutput.Builder.create(this, "TableViewerUrl")
            .value(tv.getEndpoint())
            .build();

        hcEndpoint = CfnOutput.Builder.create(this, "GatewayUrl")
            .value(gateway.getUrl())
            .build();
    }
}