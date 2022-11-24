package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Map;

public final class SampleAppCdkApp {
    public static void main(final String[] args) {
        App app = new App();

        new SampleAppCdkStack(app, "SampleAppCdkStack",
                StackProps.builder()
                        .env(Environment.builder()
                                .account("424844533422")
                                .region("us-east-1")
                                .build())
                        .build(),
                Map.of("STAGE","dev")
        );

        app.synth();
    }
}
