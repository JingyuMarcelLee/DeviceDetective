package com.devicedetective.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

@CrossOrigin
@Controller
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private final LocationService locationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final ArrayList<String> cachedClientIds;

    public WebSocketController(LocationService locationService) {
        this.locationService = locationService;
        this.cachedClientIds = new ArrayList<String>();
    }

    @MessageMapping("/sendLocation")
    @SendTo("/topic/locations")
    public Location receiveLocation(Location location) {
        logger.info("Received loc: {}", location.getLatitude());
        locationService.saveLocation(location);
        return location;
    }

    @MessageMapping("/syncLocations")
    public void handleLocationUpdates(Location location) {
//        Set<String> clientIds = cacheService.getAllCachedClientIds();

        // Save clientId to cache if non-existent
//        if(!clientIds.contains(location.getClientId())) {
//            cacheService.cacheClientId(location.getClientId());
//        }
        if(!cachedClientIds.contains(location.getClientId())){
            cachedClientIds.add(location.getClientId());
        }

        // Save to DB
        locationService.saveLocation(location);

        // Retrieve all DB per client in reverse chronological (REAL)
        // Get list of clientid - most recent location pair (DEMO)
        // Return clientId: location one per message
        logger.info("num of cached ids: {}", cachedClientIds.size());
        for(String clientId : cachedClientIds) {
            messagingTemplate.convertAndSend(
                    "/topic/locations",
                    locationService.findLocationByClientId(clientId)
            );
        }
    }

    @MessageMapping("/message")
    public void receiveMessage(String message) {
        logger.info("Received message: {}", message);
    }
}