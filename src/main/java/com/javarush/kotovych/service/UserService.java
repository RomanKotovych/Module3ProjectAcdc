package com.javarush.kotovych.service;

import com.javarush.kotovych.entity.User;
import com.javarush.kotovych.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        userRepository.create(user);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public Collection<User> getAll() {
        return userRepository.getAll();
    }

    public Optional<User> get(long id) {
        return userRepository.get(id);
    }

    public Optional<User> get(String login) {
        return userRepository.get(login);
    }

    public boolean checkIfCorrect(String login, String password) {
        Optional<User> userOptional = userRepository.get(login);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(!user.getPassword().equals(password)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean checkIfExists(long id) {
        Optional<User> userOptional = userRepository.get(id);
        if(userOptional.isPresent()) {
            return true;
        }
        return false;
    }
}
