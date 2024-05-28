package com.vashchenko.task.security;

import com.vashchenko.task.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        boolean isValidated=false;
        if(token!=null){
            isValidated = jwtService.validateAccessToken(token);
        }
        if (token != null && isValidated) {
            final JwtAuthentication jwtInfoToken = jwtService.generateAuthentication(token);
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        if(token!=null&&!isValidated){
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if(bearer==null){
            return null;
        }
        if (bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        if(bearer.isEmpty()){
            return "";
        }
        return null;
    }
}
