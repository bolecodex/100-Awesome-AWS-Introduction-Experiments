package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import software.amazon.awscdk.services.codecommit.Repository;

public class WorkshopPipelineStack extends Stack {
    public WorkshopPipelineStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public WorkshopPipelineStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        // Pipeline code goes here
        // This creates a new CodeCommit repository called 'WorkshopRepo'
        final Repository repo = Repository.Builder.create(this, "WorkshopRepo")
            .repositoryName("WorkshopRepo")
            .build();
    }
}