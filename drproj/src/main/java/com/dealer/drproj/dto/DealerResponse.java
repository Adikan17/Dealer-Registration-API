package com.dealer.drproj.dto;
import java.time.Instant;
import com.dealer.drproj.entity.*;

public class DealerResponse {
    private String name;
    private String msisdn;
    private DealerType type;
    private DealerStatus status;
    private Instant createdAt;

    public DealerResponse(String name,String msisdn,DealerType type,DealerStatus status,Instant createdAt){
        this.name=name;
        this.msisdn=msisdn;
        this.type=type;
        this.status=status;
        this.createdAt=createdAt;
    }
    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getMsisdn(){return msisdn;}
    public void setMsisdn(String msisdn){this.msisdn=msisdn;}

    public DealerType getType(){return type;}
    public void setType(DealerType type){this.type=type;}

    public DealerStatus getStatus(){return status;}
    public void setStatus(DealerStatus status){this.status=status;}

    public Instant getCreatedAt(){return createdAt;}
}
