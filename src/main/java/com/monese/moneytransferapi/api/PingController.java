package com.monese.moneytransferapi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private static Logger log = LoggerFactory.getLogger(PingController.class);

    /**
     * This api is to check if the app is running or not
     * @return returns 200OK if app is up and running.
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>("Server Running!", HttpStatus.OK);
    }
}
