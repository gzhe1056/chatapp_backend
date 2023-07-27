package com.gzhe.chatapp.service;

import com.gzhe.chatapp.exception.ChatException;
import com.gzhe.chatapp.exception.MessageException;
import com.gzhe.chatapp.exception.UserException;
import com.gzhe.chatapp.model.Chat;
import com.gzhe.chatapp.model.Message;
import com.gzhe.chatapp.model.User;
import com.gzhe.chatapp.repository.MessageRepository;
import com.gzhe.chatapp.request.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImplementation implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Override
    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException {
        User user = userService.findUserById(req.getUserId());
        Chat chat = chatService.findChatById(req.getChatId());

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimeStamp(LocalDateTime.now());
        message.setIs_read(false);

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatsMessages(Integer chatId) throws ChatException {
        Chat chat = chatService.findChatById(chatId);
        List<Message> messages = messageRepository.findMessageByChatId(chatId);

        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> message = messageRepository.findById(messageId);

        if (message.isPresent()) {
            return message.get();
        }
        throw new MessageException("Message does not exist with ID: " + messageId);
    }

    @Override
    public String deleteMessage(Integer messageId) throws MessageException {
        Message message = findMessageById(messageId);
        messageRepository.delete(message);

        return "Message deleted";
    }
}
