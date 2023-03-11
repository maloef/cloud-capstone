package com.maloef.cdnd.capstone.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTodoRequest {
    @NotBlank(message = "name is mandatory")
    private String name;
    private String dueDate;
    private boolean done;
}
