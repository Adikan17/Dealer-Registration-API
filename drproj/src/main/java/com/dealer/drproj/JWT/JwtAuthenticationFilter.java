package com.dealer.drproj.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.*;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtService jwtservice;
    public JwtAuthenticationFilter(JwtService jwtservice){this.jwtservice=jwtservice;}
    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterchain) throws ServletException,IOException{
        String authHeader=request.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterchain.doFilter(request, response);
            return;
        }
        String token=authHeader.substring(7);
        try{
            String msisdn=jwtservice.extractMsisdn(token);
            String role=jwtservice.extractRole(token);
            if(msisdn!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                if(jwtservice.isTokenValid(token, msisdn) && role!=null){
                    List<SimpleGrantedAuthority> authorities=Collections.singletonList(new SimpleGrantedAuthority(role));
                    UserDetails userd=new User(msisdn, "", authorities);
                    UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userd,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        catch(Exception e){}
        filterchain.doFilter(request, response);
    }
}
