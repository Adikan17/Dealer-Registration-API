package com.dealer.drproj.dto;
import jakarta.validation.constraints.*;

public class StatusUpdateRequest {
    @NotBlank(message="Password is mandatory.")
    private String password;
    private String status;
    private String name;
    private String type;

    public StatusUpdateRequest(){}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getType(){return type;}
    public void setType(String type){this.type=type;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}
}
