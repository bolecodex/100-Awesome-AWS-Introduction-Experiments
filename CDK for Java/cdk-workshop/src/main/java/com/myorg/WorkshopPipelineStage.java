package com.myorg;

import software.amazon.awscdk.Stage;
import software.amazon.awscdk.StageProps;
import software.constructs.Construct;

import software.amazon.awscdk.CfnOutput;

public class WorkshopPipelineStage extends Stage {

    public final CfnOutput hcViewerUrl;
    public final CfnOutput hcEndpoint;

    public WorkshopPipelineStage(Construct scope, final String id) {
        this(scope, id, null);
    }

    public WorkshopPipelineStage(Construct scope, final String id, StageProps props) {
        super(scope, id, props);

        final CdkWorkshopStack service = new CdkWorkshopStack(this, "WebSevice");
        hcViewerUrl = service.hcViewerUrl;
        hcEndpoint = service.hcEndpoint;
    }
}