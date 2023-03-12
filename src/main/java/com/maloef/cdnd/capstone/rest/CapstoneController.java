package com.maloef.cdnd.capstone.rest;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.maloef.cdnd.capstone.auth.JwtHandler;
import com.maloef.cdnd.capstone.businesslogic.TodoHandler;
import com.maloef.cdnd.capstone.domain.Todo;
import com.maloef.cdnd.capstone.exception.TodoNotFoundException;
import com.maloef.cdnd.capstone.request.CreateTodoRequest;
import com.maloef.cdnd.capstone.request.UpdateTodoRequest;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@XRayEnabled
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

    @PostMapping("/todos")
    public Map<String, Todo> createTodo(@RequestHeader (name="Authorization") String authHeader,
                                        @Valid @RequestBody CreateTodoRequest createTodoRequest) {
        String userId = jwtHandler.extractUserIdFromToken(authHeader);
        Todo todo = todoHandler.createTodo(createTodoRequest, userId);

        return Collections.singletonMap("item", todo);
    }

    @PatchMapping("/todos/{todoId}")
    public Todo updateTodo(@RequestHeader(name="Authorization") String authHeader,
                           @PathVariable("todoId") String todoId,
                           @Valid @RequestBody UpdateTodoRequest updateTodoRequest) {
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

    @ExceptionHandler
    public ResponseEntity<?> handleTodoNotFoundException(TodoNotFoundException todoNotFoundException) {
        log.error("todo not found", todoNotFoundException);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    /**
     * If there is a problem with the JWT, we let the user know what that problem is.
     */
    @ExceptionHandler
    public ResponseEntity<?> handleJwtException(JwtException jwtException) {
        log.warn("problem with jwt", jwtException);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtException.toString());
    }

    /**
     * If the request contains invalid data, we let the user know what the problem is.
     */
    @ExceptionHandler
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        log.error("validation errors: {}", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}

