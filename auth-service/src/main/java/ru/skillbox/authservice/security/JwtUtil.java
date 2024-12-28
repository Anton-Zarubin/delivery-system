package ru.skillbox.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.skillbox.authservice.domain.User;
import ru.skillbox.authservice.repository.UserRepository;

import java.util.Date;
import java.util.List;

import static ru.skillbox.authservice.security.SecurityConstants.TOKEN_PREFIX;

@Component
public class JwtUtil {

    @Value("${jwt.expiration-time}")
    private Long expirationTime;

    @Autowired
    public Algorithm algorithm;

    @Autowired
    public UserRepository userRepository;

    public Date makeExpirationDate() {
        return new Date(System.currentTimeMillis() + expirationTime);
    }

    public String generateToken(UserDetails userDetails) {
        User user = userRepository.findByName(userDetails.getUsername()).orElseThrow();
        Long userId = user.getId();

        List<String> roleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return JWT.create()
                .withIssuer("http://skillbox.ru")
                .withIssuedAt(new Date())
                .withExpiresAt(makeExpirationDate())
                .withSubject(userDetails.getUsername())
                .withClaim("id", userId)
                .withClaim("roles", String.join(", ", roleList))
                .withExpiresAt(makeExpirationDate())
                .sign(algorithm);
    }

    public String getSubjectFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
    }
}
