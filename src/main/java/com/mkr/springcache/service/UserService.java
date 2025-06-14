package com.mkr.springcache.service;

import com.mkr.springcache.model.User;
import com.mkr.springcache.repository.UserRepository;
import com.mkr.springcache.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        log.info("Find all users");
        return userRepository
            .findAll()
            .stream()
            .map(UserDto::fromDto)
            .toList();
    }

    public UserDto findById(Long id) {
        log.info("Find user by id: {}", id);
        return userRepository
            .findById(id)
            .map(UserDto::fromDto)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDto save(User user) {
        log.info("Save user: {}", user);
        var createUser = userRepository.save(user);
        return UserDto.fromDto(createUser);
    }

    public UserDto update(Long id, User user) {
        log.info("Update user: {}", user);
        user.setId(id);
        return save(user);
    }

    public void delete(Long id) {
        log.info("Delete user: {}", id);
        userRepository.deleteById(id);
    }
}
