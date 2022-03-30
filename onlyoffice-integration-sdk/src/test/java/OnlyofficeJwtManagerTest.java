import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.config.Config;
import core.security.OnlyofficeJwtManagerBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeJwtManagerTest {
    private final OnlyofficeJwtManagerBase jwtSecurity = new OnlyofficeJwtManagerBase();
    private final String secret = "secret";
    private final Config config = Config.builder().type("test").build();

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
        Optional<String> optional = this.jwtSecurity.sign(this.config, this.secret, date);
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
    }

    @Test
    public void verifyJwtObjectSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtSecurity.sign(this.config, this.secret, date).get();
        Optional<Config> optional = this.jwtSecurity.verify(this.config, token, this.secret);
        assertTrue(optional.isPresent());
        assertEquals(config.getType(), optional.get().getType());
    }

    @Test
    public void verifyJwtObjectDifferentCaseTest() {
        class Info {
            private String Info;

            public String getInfo() {
                return Info;
            }

            public void setInfo(String info) {
                Info = info;
            }
        }

        Info info = new Info();
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtSecurity.sign(Map.of(
                "info", "hello"
        ), this.secret, date).get();
        this.jwtSecurity.verify(info, token, this.secret);
        assertNotNull(info.getInfo());
    }

    @Test
    public void verifyInvalidObjectJwtSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        String token = this.jwtSecurity.sign(this.config, this.secret, date).get();
        assertThrows(JWTVerificationException.class, () -> this.jwtSecurity.verify(this.config, token, this.secret));
    }

    @Test
    public void verifyRawJwtSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtSecurity.sign(this.config, this.secret, date).get();
        this.jwtSecurity.verify(token, this.secret);
    }

    @Test
    public void verifyRawMalformedJwtSignatureTest() {
        String token = "sadasdsadsadsdascascascas.acascascas.cascascsa";
        assertThrows(JWTVerificationException.class, () -> this.jwtSecurity.verify(token, this.secret));
    }

    @Test
    public void decodeValidJwt() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtSecurity.sign(this.config, this.secret, date).get();
        assertDoesNotThrow(() -> this.jwtSecurity.decode(token));
    }

    @Test
    public void decodeMalformedJwt() {
        String token = "asdascwg1412412421rfasfsfsa.acsacsac.qw41414";
        assertThrows(JWTDecodeException.class, () -> this.jwtSecurity.decode(token));
    }

    @Test
    public void decodeValidJwtIntoObject() {
        Config c = Config.builder().build();
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtSecurity.sign(this.config, this.secret, date).get();
        this.jwtSecurity.decode(c, token);
        assertEquals("test", c.getType());
    }
}
