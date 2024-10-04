package com.httpserver.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    private HttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        httpRequest = new HttpRequest();
    }

    @Test
    void testSetMethod_GET() {
        HttpMethod method = HttpMethod.GET;
        httpRequest.setMethod(method);
        assertEquals(method, httpRequest.getMethod());
    }

    @Test
    void testSetMethod_POST() {
        HttpMethod method = HttpMethod.POST;
        httpRequest.setMethod(method);
        assertEquals(method, httpRequest.getMethod());
    }

    @Test
    void testSetMethod_PUT() {
        HttpMethod method = HttpMethod.PUT;
        httpRequest.setMethod(method);
        assertEquals(method, httpRequest.getMethod());
    }

    @Test
    void testSetMethod_DELETE() {
        HttpMethod method = HttpMethod.DELETE;
        httpRequest.setMethod(method);
        assertEquals(method, httpRequest.getMethod());
    }

    @Test
    void testSetRequestTarget_Valid() throws HttpParsingException {
        String validTarget = "/api/resource";
        httpRequest.setRequestTarget(validTarget);
        assertEquals(validTarget, httpRequest.getRequestTarget());
    }

    @Test
    void testSetRequestTarget_EmptyTarget() {
        assertThrows(HttpParsingException.class, () -> {
            httpRequest.setRequestTarget("");
        });
    }

    @Test
    void testSetRequestTarget_NullTarget() {
        assertThrows(HttpParsingException.class, () -> {
            httpRequest.setRequestTarget(null);
        });
    }

    @Test
    void testSetHttpVersion_Valid() throws BadHttpVersionException, HttpParsingException {
        String validHttpVersion = "HTTP/1.1";
        httpRequest.setHttpVersion(validHttpVersion);
        assertEquals(validHttpVersion, httpRequest.getOriginalHttpVersion());
        assertEquals(HttpVersion.HTTP_1_1, httpRequest.getBestCompatibleHttpVersion());
    }

    @Test
    void testSetHttpVersion_Invalid() {
        assertThrows(BadHttpVersionException.class, () -> {
            httpRequest.setHttpVersion("INVALID_VERSION");
        });
    }

    @Test
    void testSetHttpVersion_Unsupported() {
        assertThrows(HttpParsingException.class, () -> {
            httpRequest.setHttpVersion("HTTP/2.0");
        });
    }

    @Test
    void testAddHeader() {
        httpRequest.addHeader("Content-Type", "application/json");
        assertEquals("application/json", httpRequest.getHeaders().get("Content-Type"));
    }

    @Test
    void testSetBody() {
        String bodyContent = "This is a request body";
        httpRequest.setBody(bodyContent);
        assertEquals(bodyContent, httpRequest.getBody());
    }
}