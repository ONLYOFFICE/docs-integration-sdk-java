package core.model.converter.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

public class ConverterAsyncResponse {
    private ObjectMapper mapper = new ObjectMapper();
    private ConverterResponse response;
    private final String body;
    private final URI address;

    public ConverterAsyncResponse(String body, URI address) {
        this.body = body;
        this.address = address;
    }

    public synchronized ConverterResponse get() throws IOException {
        return this.mapper.readValue(Request.post(address)
                .connectTimeout(Timeout.of(1, TimeUnit.SECONDS))
                .responseTimeout(Timeout.of(1, TimeUnit.SECONDS))
                .bodyString(this.body, ContentType.APPLICATION_JSON)
                .setHeader("Accept", "application/json")
                .execute().returnContent().asString(), ConverterResponse.class);
    }

    public synchronized ConverterResponse getNow() throws IOException, InterruptedException {
        if (this.response != null && this.response.getEndConvert()) return this.response;
        ConverterResponse response = this.get();

        if (response.getError() < 0) return response;

        if (response.getEndConvert()) {
            this.response = response;
            return this.response;
        }

        TimeUnit.MILLISECONDS.sleep((long)(Math.random() * 1000));
        return this.getNow();
    }
}
