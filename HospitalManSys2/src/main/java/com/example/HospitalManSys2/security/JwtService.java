package com.example.HospitalManSys2.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secretKey;

    /**
     * Expected to be milliseconds (same as original project). If you prefer seconds,
     * adapt where tokens are created/checked.
     */
    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

//    private final tools.jackson.databind.ObjectMapper objectMapper =
//            new tools.jackson.databind.ObjectMapper();



    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String HMAC_ALGO = "HmacSHA256";

    public String extractUsername(String token) {
        return extractClaim(token, claims -> (String) claims.get("sub"));
    }

    public <T> T extractClaim(String token, Function<Map<String, Object>, T> claimsResolver) {
        Map<String, Object> claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        long now = Instant.now().toEpochMilli();
        Map<String, Object> payload = new HashMap<>(extraClaims);
        payload.put("sub", userDetails.getUsername());
        payload.put("iat", now);
        payload.put("exp", now + jwtExpiration);
        payload.put("role", userDetails.getAuthorities()
                .iterator().next().getAuthority());
        return buildToken(payload);
    }

    private String buildToken(Map<String, Object> payload) {
        try {
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            String headerJson = objectMapper.writeValueAsString(header);
            String payloadJson = objectMapper.writeValueAsString(payload);

            String headerB64 = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
            String payloadB64 = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

            String signingInput = headerB64 + "." + payloadB64;
            byte[] signature = hmacSha256(signingInput.getBytes(StandardCharsets.UTF_8), secretKeyBytes());
            String signatureB64 = base64UrlEncode(signature);

            return signingInput + "." + signatureB64;
        } catch (Exception e) {
            throw new RuntimeException("Failed to build JWT token", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            if (username == null) return false;
            if (!username.equals(userDetails.getUsername())) return false;
            Map<String, Object> claims = extractAllClaims(token);
            Object expObj = claims.get("exp");
            if (expObj == null) return false;

            long exp;
            if (expObj instanceof Number) {
                exp = ((Number) expObj).longValue();
            } else {
                exp = Long.parseLong(String.valueOf(expObj));
            }
            return Instant.now().toEpochMilli() < exp && username.equals(userDetails.getUsername());
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, Object> extractAllClaims(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token format");
            }
            String headerB64 = parts[0];
            String payloadB64 = parts[1];
            String signatureB64 = parts[2];

            String signingInput = headerB64 + "." + payloadB64;
            byte[] signature = base64UrlDecode(signatureB64);

            // verify signature
            byte[] expected = hmacSha256(signingInput.getBytes(StandardCharsets.UTF_8), secretKeyBytes());
            if (!java.util.Arrays.equals(signature, expected)) {
                throw new IllegalArgumentException("Invalid JWT signature");
            }

            byte[] payloadJson = base64UrlDecode(payloadB64);
            return objectMapper.readValue(payloadJson, new TypeReference<Map<String, Object>>() {});
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT token", e);
        }
    }

    // Helpers

    private byte[] secretKeyBytes() {
        // Keep this consistent with however you configure the secret.
        // The original project used a Base64 decode of the secret; if your secret is Base64-encoded,
        // replace this with: return Base64.getDecoder().decode(secretKey);
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] hmacSha256(byte[] data, byte[] key) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(new SecretKeySpec(key, HMAC_ALGO));
        return mac.doFinal(data);
    }

    private byte[] hmacSha256(byte[] data, byte[] key, int dummy) throws Exception {
        // unused; kept to illustrate overloaded forms if needed
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(new SecretKeySpec(key, HMAC_ALGO));
        return mac.doFinal(data);
    }

    private byte[] hmacSha256(byte[] data, byte[] key, boolean unused) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(new SecretKeySpec(key, HMAC_ALGO));
        return mac.doFinal(data);
    }

    private byte[] hmacSha256(byte[] data, String keyStr) throws Exception {
        return hmacSha256(data, keyStr.getBytes(StandardCharsets.UTF_8));
    }

    private byte[] hmacSha256(byte[] data, byte[] key, byte b) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(new SecretKeySpec(key, HMAC_ALGO));
        return mac.doFinal(data);
    }

    private byte[] hmacSha256(byte[] data, byte[] key, char c) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(new SecretKeySpec(key, HMAC_ALGO));
        return mac.doFinal(data);
    }

//    private byte[] hmacSha256(byte[] data, String key) throws Exception {
//        return hmacSha256(data, key.getBytes(StandardCharsets.UTF_8));
//    }

    private static String base64UrlEncode(byte[] in) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(in);
    }

    private static byte[] base64UrlDecode(String in) {
        return Base64.getUrlDecoder().decode(in);
    }


}
