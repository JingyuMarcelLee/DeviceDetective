package com.devicedetective.server;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Controller
public class WebSocketController {
    @MessageMapping("/sendLocation")
    @SendTo("/topic/locations")
    public Location receiveLocation(Location location) {
        return location;
    }
}