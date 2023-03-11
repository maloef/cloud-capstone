package com.maloef.cdnd.capstone.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class DynamoDBConfig {

    private static final String TODO_TABLE_NAME = "Todo";
    private static final String USER_ID = "userId";
    private static final String TODO_ID = "todoId";
    private static final String DUE_DATE = "dueDate";
    public static final String DUE_DATE_LSI = "DueDateLsi";
    private static final String STRING = "S";

    @Bean
    public DynamoDBMapper dynamoDBMapper(){
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().build();
        initTable(dynamoDB);

        return new DynamoDBMapper(dynamoDB);
    }

    public void initTable(AmazonDynamoDB dynamoDB) {
        if (dynamoDB.listTables().getTableNames().contains(TODO_TABLE_NAME)) {
            return;
        }
        CreateTableRequest request = new CreateTableRequest()
                .withTableName(TODO_TABLE_NAME)
                .withAttributeDefinitions(
                        new AttributeDefinition().withAttributeName(USER_ID).withAttributeType(STRING),
                        new AttributeDefinition().withAttributeName(TODO_ID).withAttributeType(STRING),
                        new AttributeDefinition().withAttributeName(DUE_DATE).withAttributeType(STRING))
                .withKeySchema(
                        new KeySchemaElement().withAttributeName(USER_ID).withKeyType(KeyType.HASH),
                        new KeySchemaElement().withAttributeName(TODO_ID).withKeyType(KeyType.RANGE))
                .withLocalSecondaryIndexes(new LocalSecondaryIndex()
                        .withIndexName(DUE_DATE_LSI)
                        .withKeySchema(
                                new KeySchemaElement().withAttributeName(USER_ID).withKeyType(KeyType.HASH),
                                new KeySchemaElement().withAttributeName(DUE_DATE).withKeyType(KeyType.RANGE))
                        .withProjection(new Projection().withProjectionType(ProjectionType.ALL)))
                .withBillingMode(BillingMode.PAY_PER_REQUEST);

        dynamoDB.createTable(request);
    }
}
