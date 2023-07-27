package com.gzhe.chatapp.service;

import com.gzhe.chatapp.exception.UserException;
import com.gzhe.chatapp.model.User;
import com.gzhe.chatapp.request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    User findUserProfile(String jwt);
    User updateUser(Integer userId, UpdateUserRequest req) throws UserException;
    User findUserById(Integer userId) throws UserException;
    List<User> searchUser(String query);
}
