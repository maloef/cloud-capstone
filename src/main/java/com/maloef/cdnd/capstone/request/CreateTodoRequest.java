package com.maloef.cdnd.capstone.request;

import lombok.Data;

@Data
public class CreateTodoRequest {

    private String name;
    private String dueDate;
}
