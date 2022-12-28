package integration;

import base.processor.preprocessor.OnlyofficeCallbackJwtPreProcessor;
import base.registry.OnlyofficeDefaultCallbackRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackPreProcessor;
import core.processor.OnlyofficeCallbackProcessor;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import core.runner.implementation.OnlyofficeSequentialCallbackRunner;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeProcessRuntimeException;
import exception.OnlyofficeRegistryHandlerRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OnlyofficeSequentialCallbackRunnerTest {
    private final OnlyofficeCallbackRegistry callbackRegistry = new OnlyofficeDefaultCallbackRegistry();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OnlyofficeJwtSecurityManager jwtManager = new OnlyofficeJwtSecurityManager(objectMapper);
    private final OnlyofficeCallbackProcessor callbackProcessor = new OnlyofficeCallbackProcessor(callbackRegistry);
    private final OnlyofficeCallbackPreProcessor callbackOnlyofficePreProcessor = new OnlyofficeCallbackJwtPreProcessor(jwtManager, "secret");
    private final OnlyofficeSequentialCallbackRunner callbackRunner = new OnlyofficeSequentialCallbackRunner(
            callbackProcessor,
            List.of(callbackOnlyofficePreProcessor),
            List.of()
    );

    @BeforeEach
    public void registerHandler() {
        OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler(){
            @Override
            public void handle(Callback callback) throws OnlyofficeRegistryHandlerRuntimeException {

            }

            @Override
            public Integer getCode() {
                return 2;
            }
        };
        this.callbackRegistry.register(callbackHandler);
    }

    @AfterEach
    public void removeHandler() {
        this.callbackRegistry.removeByCode(2);
    }

    @Test
    public void runNullCallbackRequestTest() {
        assertThrows(OnlyofficeRunnerRuntimeException.class, () -> this.callbackRunner.run(null));
    }

    @Test
    public void runEmptyCallbackRequestTest() {
        assertThrows(OnlyofficeProcessRuntimeException.class, () -> this.callbackRunner.run(
                Callback
                        .builder()
                        .build()
        ));
    }

    @Test
    @SneakyThrows
    public void runFullValidJwtTest() {
        Date expiresAt = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Callback.builder().filetype("type").build(), "secret", expiresAt).get();
        Callback callback = Callback
                .builder()
                .status(2)
                .token(token)
                .build();
        callback = this.callbackRunner.run(callback);
        assertEquals("type", callback.getFiletype());
    }
}
