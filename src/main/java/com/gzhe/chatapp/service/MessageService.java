package com.gzhe.chatapp.service;

import com.gzhe.chatapp.exception.ChatException;
import com.gzhe.chatapp.exception.MessageException;
import com.gzhe.chatapp.exception.UserException;
import com.gzhe.chatapp.model.Message;
import com.gzhe.chatapp.request.SendMessageRequest;

import java.util.List;

public interface MessageService {
    Message sendMessage(SendMessageRequest req) throws UserException, ChatException;
    List<Message> getChatsMessages(Integer chatId) throws ChatException;
    Message findMessageById(Integer messageId) throws MessageException;
    String deleteMessage(Integer messageId) throws MessageException;
}
