package com.dealer.drproj.dto;
import com.dealer.drproj.entity.DealerStatus;
import jakarta.validation.constraints.*;

public class StatusUpdateRequest {
    @NotBlank(message="Input is mandatory.")
    private DealerStatus status;

    @NotBlank(message="Password is mandatory.")
    private String password;

    public StatusUpdateRequest(){}

    public DealerStatus getStatus(){return status;}
    public void setStatus(DealerStatus status){this.status=status;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}
}
