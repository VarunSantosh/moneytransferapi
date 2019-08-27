package com.monese.moneytransferapi.api;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PingControllerTest {

    private PingController controller;

    @BeforeEach
    void setUp(){
        controller = new PingController();
    }

    @AfterEach
    void destroy() {
        controller = null;
    }

    /**
     * This test is to check the successful call to ping method.
     */
    @Test
    void testIfPingIsWorking() {
        ResponseEntity<String> response = controller.ping();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Server Running!", response.getBody());
    }
}
