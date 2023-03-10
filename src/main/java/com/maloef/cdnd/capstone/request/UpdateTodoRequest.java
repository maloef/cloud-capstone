package com.maloef.cdnd.capstone.request;

import lombok.Data;

@Data
public class UpdateTodoRequest {

    private String name;
    private String dueDate;
    private boolean done;
}
