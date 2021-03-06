package com.myorg;

import io.github.cdklabs.dynamotableviewer.TableViewer;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;

import software.amazon.awscdk.CfnOutput;

public class CdkWorkshopStack extends Stack {
    public final CfnOutput hcViewerUrl;
    public final CfnOutput hcEndpoint;
    
    public CdkWorkshopStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public CdkWorkshopStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        final Function hello = Function.Builder.create(this, "HelloHandler")
                .runtime(Runtime.NODEJS_14_X)
                .code(Code.fromAsset("lambda"))
                .handler("hello.handler")
                .build();

        final HitCounter helloWithCounter = new HitCounter(this, "HelloHitCounter", HitCounterProps.builder()
                .downstream(hello)
                .build());

        final LambdaRestApi helloapi = LambdaRestApi.Builder.create(this, "Endpoint")
                .handler(helloWithCounter.getHandler())
                .build();

        final TableViewer tv =TableViewer.Builder.create(this,"ViewerHitCount")
        .title("Hello Hits")
        .table(helloWithCounter.getTable())
        .build();

        hcViewerUrl = CfnOutput.Builder.create(this, "TableViewerUrl")
            .value(tv.getEndpoint())
            .build();

        hcEndpoint = CfnOutput.Builder.create(this, "GatewayUrl")
            .value(helloapi.getUrl())
            .build();
    }
}