package com.gzhe.chatapp.service;

import com.gzhe.chatapp.exception.ChatException;
import com.gzhe.chatapp.exception.UserException;
import com.gzhe.chatapp.model.Chat;
import com.gzhe.chatapp.request.GroupChatRequest;

import java.util.List;

public interface ChatService {
    Chat createChat(Integer reqUserId, Integer userId, boolean isGroup) throws UserException;
    Chat findChatById(Integer chatId) throws ChatException;
    List<Chat> findAllChatByUserId(Integer userId) throws UserException;
    Chat deleteChat(Integer chatId, Integer userId) throws  ChatException, UserException;
    Chat createGroup(GroupChatRequest req, Integer reqUserId) throws UserException;
    Chat addUserToGroup(Integer userId, Integer chatId) throws UserException, ChatException;
    Chat renameGroup(Integer chatId, String groupName, Integer reqUserId) throws ChatException, UserException;
    Chat removeFromGroup(Integer chatId, Integer userId, Integer reqUserId) throws UserException, ChatException;
}
