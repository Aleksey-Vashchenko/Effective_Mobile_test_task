package com.vashchenko.task.services;

import com.vashchenko.task.dto.helpers.JwtTokenPair;
import com.vashchenko.task.dto.requests.RefreshRequest;
import com.vashchenko.task.security.JwtAuthentication;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class JwtService {
    private final PrivateKey privateAccessKey;
    private final PublicKey publicAccessKey;
    private final Key privateRefreshKey;
    private final Long accessTime;
    private final Long refreshTime;

    public JwtService(
            @Value("${jwt.refreshKey}") String jwtRefreshSecret,
            @Value("${jwt.accessTime}") Long jwtAccessTine,
            @Value("${jwt.refreshTime}") Long jwtRefreshTime
    ) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        try(InputStream privateTokenStream = getClass().getClassLoader().getResourceAsStream("crypto/private_key.pem");
            InputStream publicTokenStream = getClass().getClassLoader().getResourceAsStream("crypto/public_key.pem")) {
            this.privateRefreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
            PemReader pemPrivateReader = new PemReader(new BufferedReader(new InputStreamReader(privateTokenStream)));
            PemReader pemPublicReader = new PemReader(new BufferedReader(new InputStreamReader(publicTokenStream)));
            java.security.Security.addProvider(
                    new org.bouncycastle.jce.provider.BouncyCastleProvider()
            );
            byte[] privateBytes = pemPrivateReader.readPemObject().getContent();
            byte[] publicBytes = pemPublicReader.readPemObject().getContent();

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateBytes);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicBytes);
            this.privateAccessKey = keyFactory.generatePrivate(privateKeySpec);
            this.publicAccessKey = keyFactory.generatePublic(publicKeySpec);
        }
        this.accessTime = jwtAccessTine;
        this.refreshTime = jwtRefreshTime;
    }

    public JwtTokenPair generateTokens(UUID userId) {
        final String accessToken = generateAccessToken(userId.toString());
        final String refreshToken = generateRefreshToken(accessToken);
        return new JwtTokenPair(accessToken,refreshToken);
    }

    private String generateAccessToken(String userId) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(accessTime).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        final Date creatingDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setExpiration(accessExpiration)
                .setIssuedAt(creatingDate)
                .signWith(privateAccessKey, SignatureAlgorithm.RS256)
                .claim("UUID", userId)
                .compact();
    }

    private String generateRefreshToken(String accessToken) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusDays(refreshTime).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setExpiration(accessExpiration)
                .signWith(privateRefreshKey)
                .claim("hash",accessToken.hashCode())
                .compact();
    }

    private Integer extractAccessHashFromRefreshToken(String refreshToken) throws JwtException{
        Claims claims = Jwts.parser()
                .setSigningKey(privateRefreshKey)
                .parseClaimsJws(refreshToken)
                .getBody();
        return (Integer) claims.get("hash");
    }

    private Claims extractAccessClaims(String jwtAccessToken) throws JwtException{
        try {
            return Jwts.parser()
                    .setSigningKey(publicAccessKey)
                    .parseClaimsJws(jwtAccessToken)
                    .getBody();
        }
        catch (ExpiredJwtException expiredJwtException){
            return expiredJwtException.getClaims();
        }
    }

    public String extractId(String jwtAccessToken) throws JwtException{
        return (String) extractAccessClaims(jwtAccessToken).get("UUID");
    }

    public boolean validatePair(RefreshRequest refreshRequest) {
        Integer hashFromDb = extractAccessHashFromRefreshToken(refreshRequest.refreshToken());
        Integer hashFromRequest = refreshRequest.accessToken().hashCode();
        return hashFromRequest.equals(hashFromDb);
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, privateAccessKey);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, privateRefreshKey);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public JwtAuthentication generateAuthentication(String accessToken) {
        return JwtUtils.generateAuthentication(extractAccessClaims(accessToken));
    }

    private class JwtUtils {
        public static JwtAuthentication generateAuthentication(Claims claims){
            JwtAuthentication jwtAuthentication = new JwtAuthentication();
            jwtAuthentication.setUsername(claims.getSubject());
            jwtAuthentication.setAuthenticated(true);
            return new JwtAuthentication();
        }

        private static Set<GrantedAuthority> convertRolesFromStringList(List<String> roleStrings) {
            Set<GrantedAuthority> roles = new HashSet<>();

            for (String roleString : roleStrings) {
                GrantedAuthority role = new SimpleGrantedAuthority(roleString);
                roles.add(role);
            }
            return roles;
        }
    }
}


