package com.maloef.cdnd.capstone.businesslogic;

import com.maloef.cdnd.capstone.domain.Todo;
import com.maloef.cdnd.capstone.dynamodb.TodoDao;
import com.maloef.cdnd.capstone.request.CreateTodoRequest;
import com.maloef.cdnd.capstone.request.UpdateTodoRequest;
import com.maloef.cdnd.capstone.s3.AttachmentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoHandler {

    @Value("${s3.image-bucket}")
    private String bucketName;

    private final TodoDao todoDao;
    private final AttachmentHandler attachmentHandler;

    public List<Todo> getTodosForUser(String userId) {
        return todoDao.getTodosForUser(userId);
    }

    public Todo createTodo(CreateTodoRequest createTodoRequest, String userId) {
        Todo todo = new Todo();

        todo.setUserId(userId);
        todo.setName(createTodoRequest.getName());
        todo.setDueDate(createTodoRequest.getDueDate());

        todo.setTodoId(UUID.randomUUID().toString());
        todo.setCreatedAt(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        todo.setDone(false);

        todoDao.save(todo);
        return todo;
    }

    public Todo updateTodo(UpdateTodoRequest updateTodoRequest, String userId, String todoId) {
        return todoDao.updateTodo(updateTodoRequest, userId, todoId);
    }

    public void deleteTodo(String userId, String todoId) {
        todoDao.delete(userId, todoId);
        attachmentHandler.deleteAttachment(todoId);
    }

    public String createSignedAttachmentUrl(String userId, String todoId) {
        String attachmentUrl = "https://" + bucketName + ".s3.amazonaws.com/" + todoId;
        todoDao.updateAttachmentUrl(userId, todoId, attachmentUrl);

        return attachmentHandler.createSignedUrl(todoId);
    }
}
