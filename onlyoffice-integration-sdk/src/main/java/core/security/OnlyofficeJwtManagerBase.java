package core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OnlyofficeJwtManagerBase implements OnlyofficeJwtManager {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<String> sign(Object obj, String secret, Date expiresAt) throws JWTCreationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Map<String, ?> payload = this.objectMapper.convertValue(obj, Map.class);
            return Optional.of(JWT.create()
                    .withPayload(payload)
                    .withExpiresAt(expiresAt)
                    .sign(algorithm));
        } catch (Exception e) {
            throw new JWTCreationException(e.getMessage(), e);
        }
    }

    public <T> Optional<T> verify(T wrapper, String token, String secret) throws JWTVerificationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decoded = JWT.require(algorithm)
                    .acceptLeeway(2)
                    .build()
                    .verify(token);
            return this.populateObject(wrapper, decoded.getClaims());
        } catch (Exception e) {
            throw new JWTVerificationException(e.getMessage());
        }
    }

    public void verify(String token, String secret) throws JWTVerificationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .acceptLeeway(2)
                    .build()
                    .verify(token);
        } catch (Exception e) {
            throw new JWTVerificationException(e.getMessage());
        }
    }

    private <T> Optional<T> populateObject(T wrapper, Map<String, Claim> claims) throws JWTVerificationException {
        try {
            Map<String, Field> fields = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            fields.putAll(Arrays.stream(wrapper.getClass().getDeclaredFields())
                    .collect(Collectors.toMap(Field::getName, Function.identity())));

            claims.forEach((k, v) -> {
                if (fields.containsKey(k)) {
                    Field field = fields.get(k);
                    field.setAccessible(true);
                    try {
                        field.set(wrapper, v.as(field.getType()));
                    } catch (Exception e) {
                        throw new JWTVerificationException(e.getMessage());
                    }
                }
            });

            return Optional.of(wrapper);
        } catch (Exception e) {
            throw new JWTVerificationException(e.getMessage());
        }
    }
}
