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
    private final LocationService locationService;

    public WebSocketController(LocationService locationService) {
        this.locationService = locationService;
    }

    @MessageMapping("/sendLocation")
    @SendTo("/topic/locations")
    public Location receiveLocation(Location location) {
        logger.info("Received loc: {}", location.getLatitude());
        locationService.saveLocation(location);
        return location;
    }

    @MessageMapping("/syncLocations")
    @SendTo("/topic/locations")
    public Location handleLocationUpdates(Location location) {
        // Save to DB
        locationService.saveLocation(location);

        // Save clientId to cache


        // Retrieve all DB per client in reverse chronological

        // Return clientId: location one per message

        return location;
    }

    @MessageMapping("/message")
    public void receiveMessage(String message) {
        logger.info("Received message: {}", message);
    }
}