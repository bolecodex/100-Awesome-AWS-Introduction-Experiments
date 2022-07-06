package com.myorg;

import java.util.List;
import java.util.Map;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import software.amazon.awscdk.services.codecommit.Repository;
import software.amazon.awscdk.pipelines.CodeBuildStep;
import software.amazon.awscdk.pipelines.CodePipeline;
import software.amazon.awscdk.pipelines.CodePipelineSource;
import software.amazon.awscdk.pipelines.StageDeployment;

public class WorkshopPipelineStack extends Stack {
    public WorkshopPipelineStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public WorkshopPipelineStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        // Pipeline code goes here @aws-cdk/pipelines module · AWS CDK
        // https://docs.aws.amazon.com/cdk/api/v1/docs/pipelines-readme.html
        // This creates a new CodeCommit repository called 'WorkshopRepo'
        final Repository repo = Repository.Builder.create(this, "WorkshopRepo")
            .repositoryName("WorkshopRepo")
            .build();
        // The basic pipeline declaration. This sets the initial structure of our pipeline
        final CodePipeline pipeline = CodePipeline.Builder.create(this, "Pipeline")
            .pipelineName("WorkshopPipeline")
            /*synth(...): The synthAction of the pipeline will take the source artifact generated
                in by the input and build the application based on the commands.
                This is always followed by npx cdk synth. The input of the synth step will check 
                the designated repository for source code and generate an artifact.*/
            .synth(CodeBuildStep.Builder.create("SynthStep")
                .input(CodePipelineSource.codeCommit(repo, "main"))
                .installCommands(List.of(              
                        "npm install -g aws-cdk"   // Commands to run before build
                ))
                .commands(List.of(
                        "mvn clean package",            // Language-specific build commands
                        "npx cdk synth"           // Synth command (always same)
                )).build())
            .build();

        // Import and create an instance of the WorkshopPipelineStage
        final WorkshopPipelineStage deploy = new WorkshopPipelineStage(this, "Deploy");
        // StageDeployment: A Stage consists of one or more Stacks, which will be deployed in dependency order.
        StageDeployment stageDeployment = pipeline.addStage(deploy);

        // addPost: Add an additional step to run after all of the stacks in this stage.
        stageDeployment.addPost(
            CodeBuildStep.Builder.create("TestViewerEndpoint")
                .projectName("TestViewerEndpoint")
                .commands(List.of("curl -Ssf $ENDPOINT_URL"))
                // envFromCfnOutputs: Set environment variables based on Stack Outputs.
                .envFromCfnOutputs(Map.of("ENDPOINT_URL",  deploy.hcViewerUrl))
                .build(),

            CodeBuildStep.Builder.create("TestAPIGatewayEndpoint")
                .projectName("TestAPIGatewayEndpoint")
                .envFromCfnOutputs(Map.of("ENDPOINT_URL",  deploy.hcEndpoint))
                .commands(List.of(
                        "curl -Ssf $ENDPOINT_URL",
                        "curl -Ssf $ENDPOINT_URL/hello",
                        "curl -Ssf $ENDPOINT_URL/test"
                ))
                .build()
        );
    }
}
