package dev.ali.socialmediaapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@Slf4j
public class JWTService {

    private final Algorithm accessTokenAlgorithm;
    private final Algorithm refreshTokenAlgorithm;
    private static final long ACCESS_TOKEN_EXPIRATION_MS = 300000;
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000; // 7 days


    public JWTService(@Value("${jwt.access.secret}") String accessSecret,
                      @Value("${jwt.refresh.secret}") String refreshSecret) {
        this.accessTokenAlgorithm = Algorithm.HMAC256(accessSecret.getBytes());
        this.refreshTokenAlgorithm = Algorithm.HMAC256(refreshSecret.getBytes());
    }

    public String generateAccessToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MS))
                .sign(accessTokenAlgorithm);
    }

    public String generateRefreshToken(String username) {

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_MS))
                .sign(refreshTokenAlgorithm);
    }

    public boolean validateRefreshToken(String email, String token) {
        JWTVerifier verifier = getJWTVerifier(refreshTokenAlgorithm);
        return StringUtils.isNotEmpty(email) && isTokenExpired(verifier, token);
    }

    public boolean validateAccessToken(String email, String token) {
        JWTVerifier verifier = getJWTVerifier(accessTokenAlgorithm);
        return StringUtils.isNotEmpty(email) && isTokenExpired(verifier, token);
    }

    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return !expiration.before(new Date());
    }

    private JWTVerifier getJWTVerifier(Algorithm alg) {
        JWTVerifier verifier;
        try {
            verifier = JWT.require(alg).build();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("JWT verification failed", exception);
        }
        return verifier;
    }
}


