package dev.ali.socialmediaapi.service;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTService {

    private final Date jwtExpiration = new Date(System.currentTimeMillis() + 610000000);
    private final SecretKey key = Jwts.SIG.HS256.key().build();

    public String getToken(String username) {
        return Jwts.builder().subject(username).expiration(jwtExpiration).signWith(key).compact();
    }

    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!(token.isEmpty() || token.isBlank())) {
            String PREFIX = "Bearer";

            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token.replace(PREFIX, "").trim())
                    .getPayload().getSubject();
        }
        return null;
    }


}
