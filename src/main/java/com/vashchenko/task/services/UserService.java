package com.vashchenko.task.services;

import com.vashchenko.task.entities.User;
import com.vashchenko.task.exceptions.UserIsNotFoundException;
import com.vashchenko.task.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserByLogin(String login) {
        try {
            return userRepository.findByLogin(login).get();
        }
        catch (NoSuchElementException e){
            throw new UserIsNotFoundException(login);
        }
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findUserByUuid(String userId) {
        try {
            return userRepository.findById(UUID.fromString(userId)).get();
        }
        catch (NoSuchElementException e){
            throw new UserIsNotFoundException(userId);
        }
    }
}
