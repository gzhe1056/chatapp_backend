package com.gzhe.chatapp.controller;

import com.gzhe.chatapp.exception.ChatException;
import com.gzhe.chatapp.exception.UserException;
import com.gzhe.chatapp.model.Chat;
import com.gzhe.chatapp.model.Message;
import com.gzhe.chatapp.model.User;
import com.gzhe.chatapp.request.SendMessageRequest;
import com.gzhe.chatapp.service.ChatService;
import com.gzhe.chatapp.service.MessageService;
import com.gzhe.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;

@RestController
public class RealTimeChat {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;


    @MessageMapping("/message")
    @SendTo("/group/public")
    public Message receiveMessage(@Payload com.gzhe.chatapp.model.Message message) {
        simpMessagingTemplate.convertAndSend(
                "/group/" + message
                        .getChat()
                        .getId()
                        .toString(), message);

        return message;
    }

    @MessageMapping("/chat/{groupId}")
    public Message sendToUser(@Payload SendMessageRequest request, @Header("Authorization") String jwt, @DestinationVariable String groupId) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        request.setUserId(user.getId());
        Chat chat = chatService.findChatById(request.getChatId());
        Message createdMessage = messageService.sendMessage(request);
        User receiveUser = receiver(chat, user);
        simpMessagingTemplate.convertAndSendToUser(groupId, "/private", createdMessage);

        return createdMessage;
    }

    public User receiver(Chat chat, User reqUser) {
        Iterator<User> iterator = chat.getUsers().iterator();

        User user_1 = iterator.next();
        User user_2 = iterator.next();

        if (user_1.getId().equals(reqUser.getId())) {
            return user_2;
        }
        return user_1;
    }
}
