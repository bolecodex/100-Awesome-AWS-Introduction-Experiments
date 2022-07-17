package com.myorg;

import java.util.List;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.codecommit.Repository;
import software.amazon.awscdk.pipelines.CodePipeline;
import software.amazon.awscdk.pipelines.CodeBuildStep;
import software.amazon.awscdk.pipelines.CodePipelineSource;

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
            .commands(List.of("mvn clean package", "npx cdk synth"))
            .build())
        .build();

        final WorkshopPipelineStage deploy= new WorkshopPipelineStage(this, "Depoly");
        pipeline.addStage(deploy);

    }
}

