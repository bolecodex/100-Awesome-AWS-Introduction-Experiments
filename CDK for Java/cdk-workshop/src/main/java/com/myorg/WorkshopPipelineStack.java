package com.myorg;

import java.util.List;
import java.util.Map;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.codecommit.Repository;
import software.amazon.awscdk.pipelines.CodePipeline;
import software.amazon.awscdk.pipelines.CodeBuildStep;
import software.amazon.awscdk.pipelines.CodePipelineSource;
import software.amazon.awscdk.pipelines.StageDeployment;

public class WorkshopPipelineStack extends Stack{
    public WorkshopPipelineStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public WorkshopPipelineStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);
        final Repository repo = Repository.Builder.create(this, "WorkshopRepo")
            .repositoryName("WorkshopRepo")
            .build();

        final CodePipeline pipeline = CodePipeline.Builder.create(this, "Pipeline")
            .pipelineName("WorkshopPipeline")
            .synth(CodeBuildStep.Builder.create("SynthStep")
                .input(CodePipelineSource.codeCommit(repo, "main"))
                .installCommands(List.of(
                    "npm install -g aws-cdk"
                ))
                .commands(List.of(
                    "mvn clean package", 
                    "npx cdk synth"
                )).build())
            .build();

        final WorkshopPipelineStage deploy= new WorkshopPipelineStage(this, "Deploy");
        StageDeployment stageDeployment= pipeline.addStage(deploy);

        stageDeployment.addPost(
            CodeBuildStep.Builder.create("TestViewerEndpoint")
                .commands(List.of("curl -Ssf $TestViewer_ENDPOINT_URL"))
                .envFromCfnOutputs(Map.of("TestViewer_ENDPOINT_URL", deploy.hcViewerUrl))
                .projectName("TestViewerEndpoint")
                .build(),

            CodeBuildStep.Builder.create("TestAPIGatewayEndpoint")
                .commands(List.of(
                    "curl -Ssf $TestAPIGateway_ENDPOINT_URL",
                    "curl -Ssf $TestAPIGateway_ENDPOINT_URL/hello",
                    "curl -Ssf $TestAPIGateway_ENDPOINT_URL/test"
                ))
                .envFromCfnOutputs(Map.of("TestAPIGateway_ENDPOINT_URL", deploy.hcEndpoint))
                .projectName("TestAPIGatewayEndpoint")
                .build()
        );
    }
}

