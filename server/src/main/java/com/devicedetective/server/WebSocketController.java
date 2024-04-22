package com.devicedetective.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
    public void receiveLocation(Location location) {
        logger.info("Received loc: {}", location.getLatitude());
        this.cacheClient(location.getClientId());
        locationService.saveLocation(location);
        messagingTemplate.convertAndSend("/topic/locations", location);
    }

    @MessageMapping("/syncLocations")
    public void handleLocationUpdates(Location location) {
        // Save to DB
        this.cacheClient(location.getClientId());
        locationService.saveLocation(location);

        // @TODO: Retrieve all DB per client in reverse chronological (REAL)
        // Get list of clientid - most recent location pair (DEMO)
        // Return clientId: location one per message
        logger.info("num of cached: {}", cachedClientIds.size());
        for(String cId : cachedClientIds) {
            messagingTemplate.convertAndSend(
                    "/topic/locations",
                    locationService.findLocationByClientId(cId)
            );
        }
    }

    @MessageMapping("/registerClient")
    public void registerNewClient() {
        for(String cId : cachedClientIds) {
            messagingTemplate.convertAndSend(
                    "/topic/locations",
                    locationService.findLocationByClientId(cId)
            );
        }
    }

    @MessageMapping("/message")
    public void receiveMessage(String message) {
        logger.info("Received message: {}", message);
    }

    private void cacheClient(String clientId) {
        if(!cachedClientIds.contains(clientId)){
            cachedClientIds.add(clientId);
        }
    }
}