package uk.co.epsilontechnologies.watcher;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import uk.co.epsilontechnologies.primer.Primer;
import uk.co.epsilontechnologies.primer.PrimerStatics;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static uk.co.epsilontechnologies.watcher.WatcherStatics.when;

public class WatcherTest {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Watcher watcher = new Watcher("/test", 8500, "localhost", 8501);
    private static final Primer primer = new Primer("/test", 8501);

    @BeforeClass
    public static void setUp() {
        watcher.start();
        primer.start();
    }

    @After
    public void tearDown() {
        watcher.stop();
        primer.stop();
    }

    @Test
    public void shouldCaptureParameterValue() {

        // arrange
        PrimerStatics.when(primer.get("/get")).thenReturn(PrimerStatics.response(200));
        final ParameterValueRequestCaptor parameterValueCaptor = new ParameterValueRequestCaptor("key");
        when(watcher.get("/get")).thenCapture(parameterValueCaptor);

        // act
        restTemplate.execute("http://localhost:8500/test/get?key=value", HttpMethod.GET, new TestRequestCallback(), new TestResponseExtractor());

        // assert
        assertEquals("value", parameterValueCaptor.getCapturedValue());
    }


    @Test
    public void shouldCaptureHeaderValue() {

        // arrange
        PrimerStatics.when(primer.get("/get")).thenReturn(PrimerStatics.response(200));
        final HeaderValueRequestCaptor headerValueRequestCaptor = new HeaderValueRequestCaptor("key");
        when(watcher.get("/get")).thenCapture(headerValueRequestCaptor);

        // act
        restTemplate.execute("http://localhost:8500/test/get", HttpMethod.GET, new TestRequestCallback(null, "value"), new TestResponseExtractor());

        // assert
        assertEquals("value", headerValueRequestCaptor.getCapturedValue());
    }

    @Test
    public void shouldCaptureBodyRegExValue() {

        // arrange
        PrimerStatics.when(primer.post("/post", "\\{ \"key\" : \"([a-z]{5})\" }")).thenReturn(PrimerStatics.response(200));
        final RegExBodyRequestCaptor regExBodyRequestCaptor = new RegExBodyRequestCaptor("\\{ \"key\" : \"([a-z]{5})\" }");
        when(watcher.post("/post", "\\{ \"key\" : \"([a-z]{5})\" }")).thenCapture(regExBodyRequestCaptor);

        // act
        restTemplate.execute("http://localhost:8500/test/post", HttpMethod.POST, new TestRequestCallback("{ \"key\" : \"value\" }", null), new TestResponseExtractor());

        // assert
        assertEquals(1, regExBodyRequestCaptor.getCapturedValue().size());
        assertEquals("value", regExBodyRequestCaptor.getFirstCapturedValue());
    }

    @Test
    public void shouldCaptureBodyValue() {

        // arrange
        PrimerStatics.when(primer.post("/post", "\\{ \"key\" : \"([a-z]{5})\" }")).thenReturn(PrimerStatics.response(200));
        final BodyRequestCaptor bodyRequestCaptor = new BodyRequestCaptor();
        when(watcher.post("/post", "\\{ \"key\" : \"([a-z]{5})\" }")).thenCapture(bodyRequestCaptor);

        // act
        restTemplate.execute("http://localhost:8500/test/post", HttpMethod.POST, new TestRequestCallback("{ \"key\" : \"value\" }", null), new TestResponseExtractor());

        // assert
        assertEquals("{ \"key\" : \"value\" }", bodyRequestCaptor.getCapturedValue());
    }

    @Test
    public void shouldCaptureMultipleValuesSimultaneously() {

        // arrange
        PrimerStatics.when(primer.get("/get")).thenReturn(PrimerStatics.response(200));
        final ParameterValueRequestCaptor parameterValueCaptor = new ParameterValueRequestCaptor("key");
        final HeaderValueRequestCaptor headerValueRequestCaptor = new HeaderValueRequestCaptor("key");
        when(watcher.get("/get")).thenCapture(parameterValueCaptor, headerValueRequestCaptor);

        // act
        restTemplate.execute("http://localhost:8500/test/get?key=value", HttpMethod.GET, new TestRequestCallback(null, "value"), new TestResponseExtractor());

        // assert
        assertEquals("value", parameterValueCaptor.getCapturedValue());
        assertEquals("value", headerValueRequestCaptor.getCapturedValue());
    }


    static class TestRequestCallback implements RequestCallback {

        private String body;
        private String keyHeaderValue;

        TestRequestCallback() {
            this(null, null);
        }

        TestRequestCallback(final String body, final String keyHeaderValue) {
            this.body = body;
            this.keyHeaderValue = keyHeaderValue;
        }

        @Override
        public void doWithRequest(final ClientHttpRequest clientHttpRequest) throws IOException {
            if (body != null) {
                IOUtils.write(body, clientHttpRequest.getBody());
            }
            if (keyHeaderValue != null) {
                clientHttpRequest.getHeaders().put("key", Arrays.asList(keyHeaderValue));
            }
        }

    }

    static class TestResponseExtractor implements ResponseExtractor<String> {

        @Override
        public String extractData(ClientHttpResponse clientHttpResponse) throws IOException {
            return IOUtils.toString(clientHttpResponse.getBody());
        }

    }

}

