package com.github.devik;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ScheduledEventHandler {

    private Lorem lorem;
    protected Table table;

    public ScheduledEventHandler() {
        lorem = LoremIpsum.getInstance();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);
        table = dynamoDB.getTable(System.getenv("TABLE_NAME"));
    }

    public Void handle(Map<Object, Object> input,Context context) {
        System.out.println("Entered Lambda");
        final String content = lorem.getParagraphs(10, 20);

        System.out.println("content:"+content);

        Item item = new Item()
                .withPrimaryKey("id", UUID.randomUUID().toString())
                .withString("date", new Date().toString())
                .withString("content", content);

        System.out.println("Created Item ");
        table.putItem(item);

        System.out.println("Item Saved");
        return null;
    }
}
