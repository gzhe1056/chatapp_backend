package com.gzhe.chatapp.service;

import com.gzhe.chatapp.exception.ChatException;
import com.gzhe.chatapp.exception.UserException;
import com.gzhe.chatapp.model.Chat;
import com.gzhe.chatapp.model.User;
import com.gzhe.chatapp.repository.ChatRepository;
import com.gzhe.chatapp.request.GroupChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImplementation implements ChatService {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Chat createChat(Integer reqUserId, Integer userId, boolean isGroup) throws UserException {
        User reqUser = userService.findUserById(reqUserId);
        User second_user = userService.findUserById(userId);

        Chat isChatExist = chatRepository.findSingleChatByUsersId(second_user, reqUser);

        if (isChatExist!=null) {
            return isChatExist;
        }
        Chat chat = new Chat();
        chat.setCreated_by(reqUser);
        chat.getUsers().add(reqUser);
        chat.getUsers().add(second_user);
        chat.setIs_group(false);

        return chatRepository.save(chat);
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            return chat.get();
        }
        throw new BadCredentialsException("Chat does not exist with ID: " +chatId);
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        return chatRepository.findChatByUserId(user.getId());
    }

    @Override
    public Chat deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {
        User user = userService.findUserById(userId);
        Chat chat = findChatById(chatId);

        if ((chat.getCreated_by().getId().equals(user.getId())) && !chat.getIs_group()) {
            chatRepository.deleteById(chat.getId());
            return chat;
        }
        throw new ChatException("You don't have access to delete this chat");
    }

    @Override
    public Chat createGroup(GroupChatRequest req, Integer reqUserId) throws UserException {
        User reqUser = userService.findUserById(reqUserId);
        Chat chat = new Chat();

        chat.setCreated_by(reqUser);
        chat.getUsers().add(reqUser);

        for (Integer userId: req.getUserIds()) {
            User user = userService.findUserById(userId);
            if (user!=null){
                chat.getUsers().add(user);
            }
        }

        chat.setChat_name(req.getChat_name());
        chat.setChat_image(req.getChat_image());
        chat.setIs_group(true);
        chat.getAdmins().add(reqUser);

        return chatRepository.save(chat);
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId) throws UserException, ChatException {
        Chat chat = findChatById(chatId);
        User user = userService.findUserById(userId);

        chat.getUsers().add(user);

        return chatRepository.save(chat);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, Integer reqUserId) throws ChatException, UserException {
        Chat chat = findChatById(chatId);
        User user = userService.findUserById(reqUserId);

        if (chat.getUsers().contains(user)) {
            chat.setChat_name(groupName);
        }
        return chatRepository.save(chat);
    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, Integer reqUserId) throws UserException, ChatException {
        Chat chat = findChatById(chatId);
        User user = userService.findUserById(userId);
        User reqUser = userService.findUserById(reqUserId);

        if (user.getId().equals(reqUser.getId())) {
            chat.getUsers().remove(user);
        }
        return chatRepository.save(chat);
    }
}
