package com.myorg;

import software.amazon.awscdk.App;

public final class CdkWorkshopApp {
    public static void main(final String[] args) {
        App app = new App();

        // Entry point to deploy our application stack
        // new WorkshopPipelineStack(app, "PipelineStack");
        new CdkWorkshopStack(app, "CdkWorkshopStack");

        // 构建Cloudformation模版
        app.synth();
    }
}
