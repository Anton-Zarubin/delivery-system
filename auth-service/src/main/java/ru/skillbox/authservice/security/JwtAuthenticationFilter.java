package ru.skillbox.authservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.skillbox.authservice.domain.User;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.skillbox.authservice.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                                   UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        User credentials;
        try {
            credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
        } catch (Exception ex) {
            logger.error("Can't authenticate user", ex);
            throw new InternalAuthenticationServiceException("Can't authenticate user");
        }

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                credentials.getName(),
                credentials.getPassword(),
                userDetailsService.loadUserByUsername(credentials.getName()).getAuthorities()
        );
        return authenticationManager.authenticate(userToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {
        String token = jwtUtil.generateToken(((UserDetails) authResult.getPrincipal()));
        response.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token);
    }
}
