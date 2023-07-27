package com.gzhe.chatapp.service;

import com.gzhe.chatapp.config.JwtTokenProvider;
import com.gzhe.chatapp.exception.UserException;
import com.gzhe.chatapp.model.User;
import com.gzhe.chatapp.repository.UserRepository;
import com.gzhe.chatapp.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public User findUserProfile(String jwt) {
        String email = jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> opt = userRepository.findByEmail(email);

        if (opt.isPresent()) {
            return opt.get();
        }
        throw new BadCredentialsException("Invalid token");
    }

    @Override
    public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
        User user = findUserById(userId);
        if (req.getFull_name() != null) {
            user.setFull_name(req.getFull_name());
        }
        if (req.getProfile_picture() != null) {
            user.setProfile_picture(req.getProfile_picture());
        }
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Integer userId) throws UserException {
        Optional<User> opt = userRepository.findById(userId);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new UserException("User does not exist with ID: " + userId);
    }

    @Override
    public List<User> searchUser(String query) {
        return userRepository.searchUsers(query);
    }
}
