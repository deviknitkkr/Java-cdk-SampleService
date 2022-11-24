package com.myorg;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.Schedule;
import software.amazon.awscdk.services.events.targets.LambdaFunction;
import software.amazon.awscdk.services.events.targets.SqsQueue;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.sqs.QueueEncryption;
import software.constructs.Construct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static software.amazon.awscdk.services.sqs.QueueEncryption.UNENCRYPTED;

public class SampleAppCdkStack extends Stack {
    public SampleAppCdkStack(final Construct parent, final String id) {
        this(parent, id, null, null);
    }

    public SampleAppCdkStack(final Construct parent, final String id, final StackProps props, Map<String, String> additionalProps) {
        super(parent, id, props);

        String stage = additionalProps.get("STAGE");

        final Bucket bucket = Bucket.Builder.create(this, stage + "-SampleAppBucket")
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        final String tableName = stage + "-Notes";
        final Table table = Table.Builder.create(this, stage + "-NotesTable")
                .tableName(tableName)
                .partitionKey(Attribute.builder()
                        .name("id")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("date")
                        .type(AttributeType.STRING)
                        .build())
                .removalPolicy(RemovalPolicy.DESTROY)
                .readCapacity(1)
                .writeCapacity(1)
                .build();

        HashMap<String, String> environment = new HashMap<>();
        environment.put("REGION", props.getEnv().getRegion());
        environment.put("STAGE", stage);
        environment.put("S3BUCKET", bucket.getBucketName());
        environment.put("TABLE_NAME", tableName);

        final Function eventListenerLambda = Function.Builder.create(this, stage + "-EventListenerLambda")
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("../SampleAppService/target/lambda.jar"))
                .handler("com.github.devik.ScheduledEventHandler::handle")
                .timeout(Duration.minutes(10))
                .memorySize(512)
                .environment(environment)
                .build();
        table.grantFullAccess(eventListenerLambda);

        final Queue queue = Queue.Builder.create(this, stage + "-SampleAppCdkQueue")
                .visibilityTimeout(Duration.seconds(300))
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        Rule.Builder.create(this, stage + "-schedulingRule")
                .schedule(Schedule.rate(Duration.minutes(1)))
                .targets(List.of(
                                LambdaFunction.Builder.create(eventListenerLambda).build(),
                                SqsQueue.Builder.create(queue).build()
                        )
                ).build();



//
//        final Topic topic = Topic.Builder.create(this, "SampleAppCdkTopic")
//                .displayName("My First Topic Yeah")
//                .build();
//

//        bucket.addEventNotification(
//                EventType.OBJECT_CREATED,
//                new SnsDestination(topic)
//        );
//        topic.addSubscription(new SqsSubscription(queue));
//        topic.addSubscription(EmailSubscription.Builder.create("vikaschoudhary1079066@gmail.com").build());
    }
}
