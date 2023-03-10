package com.maloef.cdnd.capstone.rest;

import com.maloef.cdnd.capstone.auth.JwtHandler;
import com.maloef.cdnd.capstone.businesslogic.TodoHandler;
import com.maloef.cdnd.capstone.domain.Todo;
import com.maloef.cdnd.capstone.exception.TodoNotFoundException;
import com.maloef.cdnd.capstone.request.CreateTodoRequest;
import com.maloef.cdnd.capstone.request.UpdateTodoRequest;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CapstoneController {

    private final JwtHandler jwtHandler;
    private final TodoHandler todoHandler;

    @GetMapping("/todos")
    public Map<String, List<Todo>> getTodos(@RequestHeader (name="Authorization") String authHeader) {
        String userId = jwtHandler.extractUserIdFromToken(authHeader);
        List<Todo> todos = todoHandler.getTodosForUser(userId);

        return Collections.singletonMap("items", todos);
    }

    @PostMapping("/todo")
    public Todo createTodo(@RequestHeader (name="Authorization") String authHeader, @RequestBody CreateTodoRequest createTodoRequest) {
        String userId = jwtHandler.extractUserIdFromToken(authHeader);

        return todoHandler.createTodo(createTodoRequest, userId);
    }

    @PatchMapping("/todos/{todoId}")
    public Todo updateTodo(@RequestHeader(name="Authorization") String authHeader,
                           @PathVariable("todoId") String todoId,
                           @RequestBody UpdateTodoRequest updateTodoRequest) {
        String userId = jwtHandler.extractUserIdFromToken(authHeader);

        return todoHandler.updateTodo(updateTodoRequest, userId, todoId);
    }

    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<?> deleteTodo(@RequestHeader(name="Authorization") String authHeader, @PathVariable("todoId") String todoId) {
        String userId = jwtHandler.extractUserIdFromToken(authHeader);
        todoHandler.deleteTodo(userId, todoId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/todos/{todoId}/attachment")
    public Map<String, String> createSignedAttachmentUrl(@RequestHeader(name="Authorization") String authHeader, @PathVariable("todoId") String todoId) {
        String userId = jwtHandler.extractUserIdFromToken(authHeader);
        String uploadUrl = todoHandler.createSignedAttachmentUrl(userId, todoId);

        return Collections.singletonMap("uploadUrl", uploadUrl);
    }

    /**
     * If there is a problem with the JWT, we let the user know what that problem is.
     */
    @ExceptionHandler
    public ResponseEntity<?> handleJwtException(JwtException jwtException) {
        log.warn("problem with jwt", jwtException);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtException.toString());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleTodoNotFoundException(TodoNotFoundException todoNotFoundException) {
        log.error("todo not found", todoNotFoundException);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

