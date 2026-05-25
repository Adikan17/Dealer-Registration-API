package com.dealer.drproj.dto;

public class LoginRequest {
    private String msisdn;
    private String token;
    private String role;
    public LoginRequest(String msisdn,String token,String role){this.msisdn=msisdn;this.token=token;this.role=role;}

    public String getMsisdn(){return msisdn;}
    public void setMsisdn(String msisdn){this.msisdn=msisdn;}

    public String getToken(){return token;}
    public void setToken(String token){this.token=token;}

    public String getRole(){return role;}
    public void setType(String role){this.role=role;}
}
