# Welcome to your CDK Java project!

You should explore the contents of this project. It demonstrates a CDK app with an instance of a stack (`SampleAppCdkStack`)
which contains an Amazon SQS queue that is subscribed to an Amazon SNS topic.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

## Build and Deploy

 1. `cdk bootstrap --profile xyz --verbose`    (Run only once) Bootstrap the environment (Create all roles required by cdk)
 2. `cdk synth --profile xyz --verbose`        (Optional) emits the synthesized CloudFormation template
 3. `cdk deploy --profile xyz --verbose`       (Run on every change) deploy this stack to your defined profile AWS account/region

Enjoy!
