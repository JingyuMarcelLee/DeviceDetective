package com.devicedetective.server;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin
@Controller
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/sendLocation")
    @SendTo("/topic/locations")
    public Location receiveLocation(Location location) {
        logger.info("Received loc: {}", location.getLatitude());

        return location;
    }

    @MessageMapping("/message")
    public void receiveMessage(String message) {
        logger.info("Received message: {}", message);
    }
}