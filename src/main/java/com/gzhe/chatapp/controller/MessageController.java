package com.gzhe.chatapp.controller;

import com.gzhe.chatapp.controller.mapper.MessageDtoMapper;
import com.gzhe.chatapp.dto.MessageDto;
import com.gzhe.chatapp.exception.ChatException;
import com.gzhe.chatapp.exception.MessageException;
import com.gzhe.chatapp.exception.UserException;
import com.gzhe.chatapp.model.Message;
import com.gzhe.chatapp.model.User;
import com.gzhe.chatapp.request.SendMessageRequest;
import com.gzhe.chatapp.response.ApiResponse;
import com.gzhe.chatapp.service.MessageService;
import com.gzhe.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<MessageDto> sendMessageHandler(@RequestHeader("Authorization") String jwt, @RequestBody SendMessageRequest request) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        request.setUserId(request.getUserId());
        Message message = messageService.sendMessage(request);
        MessageDto messageDto = MessageDtoMapper.toMessageDto(message);

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId]")
    public ResponseEntity<List<MessageDto>> getChatsMessageHandler(@PathVariable Integer chatId) throws ChatException {
        List<Message> messages = messageService.getChatsMessages(chatId);
        List<MessageDto> messageDtos = MessageDtoMapper.toMessageDtos(messages);

        return new ResponseEntity<>(messageDtos, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer messageId) throws MessageException {
        messageService.deleteMessage(messageId);
        ApiResponse response = new ApiResponse("Message deleted", true);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
