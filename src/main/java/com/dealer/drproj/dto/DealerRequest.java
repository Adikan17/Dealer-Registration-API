package com.dealer.drproj.dto;
import com.dealer.drproj.entity.DealerType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class DealerRequest {
    @NotBlank(message="Name is a mandatory feild.")
    private String name;

    @NotBlank(message="MSISDN is a mandatory feild.")
    @Pattern(
        regexp="\\d{10}",
        message="MSISDN must exactly be of length 10 digits."
    )
    private String msisdn;

    @NotBlank(message="Password is a mandatory field.")
    private String password;

    @NotNull(message="Type is a mandatory feild.")
    private DealerType type;

    public DealerRequest(){}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getMsisdn(){return msisdn;}
    public void setMsisdn(String msisdn){this.msisdn=msisdn;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}

    public DealerType getType(){return type;}
    public void setType(DealerType type){this.type=type;}
}
