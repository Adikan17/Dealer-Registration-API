package com.dealer.drproj.dto;

public class LoginResponse {
    private String token;
    private String msisdn;
    private String role;

    public LoginResponse(String token, String msisdn, String role) {
        this.token = token;
        this.msisdn = msisdn;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getMsisdn() { return msisdn; }
    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}