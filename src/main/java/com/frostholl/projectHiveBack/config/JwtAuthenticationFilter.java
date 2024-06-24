package com.frostholl.projectHiveBack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frostholl.projectHiveBack.exception.auth.AuthenticationExceptionHandler;
import com.frostholl.projectHiveBack.exception.auth.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userLogin;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        userLogin = jwtService.extractUsername(jwtToken);
        if (userLogin != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = this.userDetailsService.loadUserByUsername(userLogin);
                if (jwtService.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            catch (UserNotFoundException ex) {
                var mes = authenticationExceptionHandler.handleUserNotFoundException(ex);
                response.setContentType("application/json");
                response.setStatus(404);
                String json = new ObjectMapper().writeValueAsString(mes.getBody());
                response.getWriter().write(json);
                response.flushBuffer();
                return;
            }
            catch (ExpiredJwtException ex) {
                var mes = authenticationExceptionHandler.handleExpiredJwtException(ex);
                response.setContentType("application/json");
                response.setStatus(409);
                String json = new ObjectMapper().writeValueAsString(mes.getBody());
                response.getWriter().write(json);
                response.flushBuffer();
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
