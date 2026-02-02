package com.example.expense_tracker.config;

import com.example.expense_tracker.service.CustomUserDetailsService;
import com.example.expense_tracker.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException{

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        //1. looking for bearer token in header
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
            //debug2
            System.out.println("DEBUG: Token detected. Username: " + username);
        }

        //2. if username fouund & user is not logged in
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //getting user details from CustomUserDetailsService
            UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);

            // validate token against user details
            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // set user in security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        // 5. handing of request to next filter in chain
        // debug1
        System.out.println("DEBUG: SecurityContext Authentication: " + SecurityContextHolder.getContext().getAuthentication());
        filterChain.doFilter(request, response);
    }
}
