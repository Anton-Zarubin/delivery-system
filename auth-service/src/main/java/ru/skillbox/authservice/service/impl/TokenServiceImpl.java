package ru.skillbox.authservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.skillbox.authservice.dto.UserDto;
import ru.skillbox.authservice.security.JwtUtil;
import ru.skillbox.authservice.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    @Autowired
    public TokenServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String generateToken(UserDto loginUser) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginUser.getName());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getName(),
                        loginUser.getPassword(),
                        userDetails.getAuthorities()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtil.generateToken(userDetails);
    }
}
