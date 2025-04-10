package com.example;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;


@Controller
public class NotificationController {

    private final ReceiveSendNotifications msgService;

    public NotificationController(ReceiveSendNotifications msgService) {
        this.msgService = msgService;
    }

    // send emails for all parsable notifications
    @GetMapping("/process")
    @ResponseBody
    List<String> processNotifications(HttpServletRequest request, HttpServletResponse response) {
        return msgService.processNotifications();
    }


    //  Lists all message bodies
    @GetMapping("/list")
    @ResponseBody
    List<HashMap<String, String>> listMessages(HttpServletRequest request, HttpServletResponse response) {
        return msgService.listMessages();
    }


    //  Purge the message queue
    @GetMapping("/purge")
    @ResponseBody
    void purgeQueue(HttpServletRequest request, HttpServletResponse response) {
        msgService.purgeQueue();
    }

}