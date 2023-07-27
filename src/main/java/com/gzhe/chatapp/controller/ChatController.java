package com.gzhe.chatapp.controller;

import com.gzhe.chatapp.controller.mapper.ChatDtoMapper;
import com.gzhe.chatapp.dto.ChatDto;
import com.gzhe.chatapp.exception.ChatException;
import com.gzhe.chatapp.exception.UserException;
import com.gzhe.chatapp.model.Chat;
import com.gzhe.chatapp.model.User;
import com.gzhe.chatapp.request.RenameGroupRequest;
import com.gzhe.chatapp.request.SingleChatRequest;
import com.gzhe.chatapp.service.ChatService;
import com.gzhe.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @PostMapping("/single")
    public ResponseEntity<ChatDto> createChatHandler(@RequestBody SingleChatRequest singleChatRequest, @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createChat(reqUser.getId(), singleChatRequest.getUserId(), false);
        ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

        return new ResponseEntity<>(chatDto, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<ChatDto> createGroupHandler(@PathVariable Integer chatId) throws ChatException {
        Chat chat = chatService.findChatById(chatId);
        ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

        return new ResponseEntity<>(chatDto, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ChatDto>> findAllChatByUserIdHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfile(jwt);
        List<Chat> chats = chatService.findAllChatByUserId(user.getId());
        List<ChatDto> chatDtos = ChatDtoMapper.toChatDtos(chats);

        return new ResponseEntity<>(chatDtos, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<ChatDto> addUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId) throws UserException, ChatException {
        Chat chat = chatService.addUserToGroup(userId, chatId);
        ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

        return new ResponseEntity<>(chatDto, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/rename")
    public ResponseEntity<ChatDto> renameGroupHandler(@PathVariable Integer chatId, @RequestBody RenameGroupRequest renameGroupRequest, @RequestHeader("Authorization") String jwt) throws ChatException, UserException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.renameGroup(chatId, renameGroupRequest.getGroupName(), reqUser.getId());
        ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

        return new ResponseEntity<>(chatDto, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<ChatDto> removeFromGroupHandler(@RequestHeader("Authorization") String jwt, @PathVariable Integer chatId, @PathVariable Integer userId) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.removeFromGroup(chatId, userId, reqUser.getId());
        ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

        return new ResponseEntity<ChatDto>(chatDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}/{userId}")
    public ResponseEntity<ChatDto> deleteChatHandler(@PathVariable Integer chatId, @PathVariable Integer userId) throws ChatException, UserException {
        Chat chat = chatService.deleteChat(chatId, userId);
        ChatDto chatDto = ChatDtoMapper.toChatDto(chat);

        return new ResponseEntity<ChatDto>(chatDto, HttpStatus.OK);
    }
}