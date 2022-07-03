package com.myorg;

import software.amazon.awscdk.Stage;
import software.constructs.Construct;
import software.amazon.awscdk.StageProps;
import software.amazon.awscdk.CfnOutput;

// Declare a new Stage (component of a pipeline), and instantiate application stack.
public class WorkshopPipelineStage extends Stage {

    // 声明WorkshopPipelineStage类的属性
    public final CfnOutput hcViewerUrl;
    public final CfnOutput hcEndpoint;

    public WorkshopPipelineStage(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public WorkshopPipelineStage(final Construct scope, final String id, final StageProps props) {
        super(scope, id, props);

        // WorkshopPipelineStage is the scope of CdkWorkshopStack
        final CdkWorkshopStack service = new CdkWorkshopStack(this, "WebService");
        // 为WorkshopPipelineStage类的属性赋值
        hcViewerUrl = service.hcViewerUrl;
        hcEndpoint = service.hcEndpoint;
    }
}