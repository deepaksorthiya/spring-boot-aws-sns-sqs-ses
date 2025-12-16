package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;


@RestController
public class NotificationController {

    private final ReceiveSendNotifications msgService;

    public NotificationController(ReceiveSendNotifications msgService) {
        this.msgService = msgService;
    }

    // send emails for all parsable notifications
    @GetMapping("/process")
    List<String> processNotifications() {
        return msgService.processNotifications();
    }


    //  Lists all message bodies
    @GetMapping("/list")
    List<HashMap<String, String>> listMessages() {
        return msgService.listMessages();
    }


    //  Purge the message queue
    @GetMapping("/purge")
    void purgeQueue() {
        msgService.purgeQueue();
    }

}