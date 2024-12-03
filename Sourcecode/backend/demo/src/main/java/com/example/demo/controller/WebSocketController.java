package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;

    }
    
    @GetMapping("/send-message")
    public String handleWebSocketMessage(@RequestParam String message){
        System.out.println("Received message: " + message);
        messagingTemplate.convertAndSend("/topic/messages", message);
        return "Message sent: " + message;
    }

}
