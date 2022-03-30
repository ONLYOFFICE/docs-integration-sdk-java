package core.security;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;
import java.util.Optional;

public interface OnlyofficeJwtManager {
    /**
     *
     * @param payload
     * @param secret
     * @param expiresAt
     * @param <T>
     * @return
     */
    <T> Optional<String> sign(T payload, String secret, Date expiresAt) throws JWTCreationException;

    /**
     *
     * @param wrapper
     * @param token
     * @param secret
     * @param <T>
     * @return
     */
    <T> Optional<T> verify(T wrapper, String token, String secret) throws JWTVerificationException;

    /**
     *
     * @param token
     * @param secret
     * @throws JWTVerificationException
     */
    void verify(String token, String secret) throws JWTVerificationException;

    /**
     *
     * @param token
     * @throws JWTDecodeException
     */
    void decode(String token) throws JWTDecodeException;

    /**
     *
     * @param wrapper
     * @param token
     * @param <T>
     * @return
     * @throws JWTDecodeException
     */
    <T> Optional<T> decode(T wrapper, String token) throws JWTDecodeException;
}
