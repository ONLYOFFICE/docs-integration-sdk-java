package unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.model.config.Config;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeJwtDecodingRuntimeException;
import exception.OnlyofficeJwtVerificationRuntimeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeJwtManagerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OnlyofficeJwtSecurityManager jwtSecurity = new OnlyofficeJwtSecurityManager(objectMapper);
    private final String secret = "secret";

    @Test
    public void jwtMapSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        Optional<String> optional = this.jwtSecurity.sign(Map.of("key", "value"), this.secret, date);
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
    }

    @Test
    public void jwtMapValidateTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        Optional<String> optional = this.jwtSecurity.sign(Map.of("key", "value"), this.secret, date);
        String token = optional.get();
        assertDoesNotThrow(() -> this.jwtSecurity.verify(token, this.secret));
    }

    @Test
    public void jwtObjectSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        Config config = Config.builder().type("type").build();
        Optional<String> optional = this.jwtSecurity.sign(config, this.secret, date);
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
    }

    @Test
    public void verifyJwtObjectSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        Config config = Config.builder().type("type").build();
        String token = this.jwtSecurity.sign(config, this.secret, date).get();
        config.setType("c");
        this.jwtSecurity.verify(config, token, this.secret);
        assertEquals("type", config.getType());
    }

    @Test
    public void verifyInvalidObjectJwtSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        Config config = Config.builder().type("type").build();
        String token = this.jwtSecurity.sign(config, this.secret, date).get();
        assertThrows(OnlyofficeJwtVerificationRuntimeException.class, () -> this.jwtSecurity.verify(config, token, this.secret));
    }

    @Test
    public void verifyRawJwtSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        Config config = Config.builder().type("type").build();
        String token = this.jwtSecurity.sign(config, this.secret, date).get();
        assertDoesNotThrow(() -> this.jwtSecurity.verify(token, this.secret));
    }

    @Test
    public void verifyRawMalformedJwtSignatureTest() {
        String token = "sadasdsadsadsdascascascas.acascascas.cascascsa";
        assertThrows(OnlyofficeJwtVerificationRuntimeException.class, () -> this.jwtSecurity.verify(token, this.secret));
    }

    @Test
    public void decodeValidJwt() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        Config config = Config.builder().type("type").build();
        String token = this.jwtSecurity.sign(config, this.secret, date).get();
        assertDoesNotThrow(() -> this.jwtSecurity.decode(config, token));
    }

    @Test
    public void decodeMalformedJwt() {
        String token = "asdascwg1412412421rfasfsfsa.acsacsac.qw41414";
        assertThrows(OnlyofficeJwtDecodingRuntimeException.class, () -> this.jwtSecurity.decode(token));
    }

    @Test
    public void decodeValidJwtIntoObject() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        Config config = Config.builder().type("type").build();
        String token = this.jwtSecurity.sign(config, this.secret, date).get();
        config.setType("c");
        this.jwtSecurity.decode(config, token);
        assertEquals("type", config.getType());
    }
}
