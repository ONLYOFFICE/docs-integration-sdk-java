package core.security;

import exception.OnlyofficeJwtDecodingRuntimeException;
import exception.OnlyofficeJwtSigningRuntimeException;
import exception.OnlyofficeJwtVerificationRuntimeException;

import java.util.Date;
import java.util.Optional;

public interface OnlyofficeJwtSecurity {
    /**
     *
     * @param payload
     * @param secret
     * @param expiresAt
     * @param <T>
     * @return
     * @throws OnlyofficeJwtSigningRuntimeException
     */
    <T> Optional<String> sign(T payload, String secret, Date expiresAt) throws OnlyofficeJwtSigningRuntimeException;

    /**
     *
     * @param wrapper
     * @param token
     * @param secret
     * @param <T>
     * @return
     * @throws OnlyofficeJwtVerificationRuntimeException
     */
    <T> Optional<T> verify(T wrapper, String token, String secret) throws OnlyofficeJwtVerificationRuntimeException;

    /**
     *
     * @param token
     * @param secret
     * @throws OnlyofficeJwtVerificationRuntimeException
     */
    void verify(String token, String secret) throws OnlyofficeJwtVerificationRuntimeException;

    /**
     *
     * @param token
     * @throws OnlyofficeJwtDecodingRuntimeException
     */
    void decode(String token) throws OnlyofficeJwtDecodingRuntimeException;

    /**
     *
     * @param wrapper
     * @param token
     * @param <T>
     * @return
     * @throws OnlyofficeJwtDecodingRuntimeException
     */
    <T> Optional<T> decode(T wrapper, String token) throws OnlyofficeJwtDecodingRuntimeException;
}
