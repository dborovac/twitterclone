package com.twitterclone.iam.api.controller;

import com.twitterclone.iam.domain.FollowNotification;
import com.twitterclone.iam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final UserService userService;

    @MessageMapping("/follows")
    @SendTo("/topic/follow-notifications")
    public Flux<FollowNotification> followNotifications(@Payload String userId) {
        return userService.getFollowNotifications(userId);
    }
}
