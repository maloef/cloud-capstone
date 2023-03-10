package com.maloef.cdnd.capstone.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Data;

@DynamoDBTable(tableName = "Todo")
@Data
public class Todo {

    @DynamoDBHashKey
    private String userId;
    @DynamoDBRangeKey
    private String todoId;
    @DynamoDBAttribute
    private String name;
    @DynamoDBAttribute
    private String createdAt;
    @DynamoDBAttribute
    @DynamoDBIndexRangeKey(localSecondaryIndexName = "DueDateLsi")
    private String dueDate;
    @DynamoDBAttribute
    private boolean done;
    @DynamoDBAttribute
    private String attachmentUrl;
}
