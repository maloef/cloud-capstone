package com.maloef.cdnd.capstone.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.maloef.cdnd.capstone.config.DynamoDBConfig;
import com.maloef.cdnd.capstone.domain.Todo;
import com.maloef.cdnd.capstone.exception.TodoNotFoundException;
import com.maloef.cdnd.capstone.request.UpdateTodoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@XRayEnabled
@RequiredArgsConstructor
@Slf4j
public class TodoDao {

    private final DynamoDBMapper dynamoDBMapper;

    /**
     * Returns the todos for the current user sorted by dueDate.
     */
    public List<Todo> getTodosForUser(String userId) {
        Todo todo = new Todo();
        todo.setUserId(userId);

        DynamoDBQueryExpression<Todo> query = new DynamoDBQueryExpression<>();
        query.setHashKeyValues(todo);
        query.setIndexName(DynamoDBConfig.DUE_DATE_LSI);

        List<Todo> todos = dynamoDBMapper.query(Todo.class, query);
        log.info("found {} todos for user {}", todos.size(), userId);

        return todos;
    }

    public void save(Todo todo) {
        dynamoDBMapper.save(todo);
        log.info("saved todo with todoId {} and name {}", todo.getTodoId(), todo.getName());
    }

    public Todo updateTodo(UpdateTodoRequest updateTodoRequest, String userId, String todoId) {
        Todo todo = find(userId, todoId);

        todo.setName(updateTodoRequest.getName());
        todo.setDueDate(updateTodoRequest.getDueDate());
        todo.setDone(updateTodoRequest.isDone());
        dynamoDBMapper.save(todo);

        log.info("updated todo with todoId {} and name {}", todo.getTodoId(), todo.getName());
        return todo;
    }

    public void updateAttachmentUrl(String userId, String todoId, String attachmentUrl) {
        Todo todo = find(userId, todoId);

        todo.setAttachmentUrl(attachmentUrl);
        dynamoDBMapper.save(todo);

        log.info("set attachmentUrl for todoId {} to {}", todo.getTodoId(), attachmentUrl);
    }

    public void delete(String userId, String todoId) {
        Todo todo = find(userId, todoId);
        dynamoDBMapper.delete(todo);
        log.info("deleted todo with todoId {} and name {}", todo.getTodoId(), todo.getName());
    }

    private Todo find(String userId, String todoId) {
        Todo todo = dynamoDBMapper.load(Todo.class, userId, todoId);
        if (todo == null) {
            throw new TodoNotFoundException("todo with userId " + userId + " and todoId " + todoId + " does not exist");
        }
        return todo;
    }
}
