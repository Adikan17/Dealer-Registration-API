package com.dealer.drproj.dto;
import com.dealer.drproj.entity.DealerStatus;
import jakarta.validation.constraints.NotNull;;

public class StatusUpdateRequest {
    @NotNull(message="Input is mandatory.")
    private DealerStatus status;

    public StatusUpdateRequest(){}

    public DealerStatus getStatus(){return status;}
    public void steStatus(DealerStatus status){this.status=status;}
}
