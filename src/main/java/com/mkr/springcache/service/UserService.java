package com.mkr.springcache.service;

import com.mkr.springcache.model.User;
import com.mkr.springcache.repository.UserRepository;
import com.mkr.springcache.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheManager = "redisCacheManager")
public class UserService {

    private final UserRepository userRepository;

    @Cacheable("users")
    public List<UserDto> findAll() {
        log.info("Find all users");
        return userRepository
            .findAll()
            .stream()
            .map(UserDto::fromDto)
            .toList();
    }

    @Cacheable(value = "userById", key = "#id")
    public UserDto findById(Long id) {
        log.info("Find user by id: {}", id);
        return userRepository
            .findById(id)
            .map(UserDto::fromDto)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

//    Удаляем все записи из кэша при вызове метода save
    @CacheEvict(value = "users", allEntries = true)
    public UserDto save(User user) {
        log.info("Save user: {}", user);
        var createUser = userRepository.save(user);
        return UserDto.fromDto(createUser);
    }

    @Caching(evict = {
//            Удаляем все записи из кэша при вызове метода save
            @CacheEvict(value = "users", allEntries = true),
//            Здесь не нужно удалять все записи из кэша, а только по определенному ключу
            @CacheEvict(value = "userById", key = "#id")
    })
    public UserDto update(Long id, User user) {
        log.info("Update user: {}", user);
        user.setId(id);
        return save(user);
    }

    @Caching(evict = {
//            Удаляем все записи из кэша при вызове метода save
            @CacheEvict(value = "users", allEntries = true),
//            Здесь не нужно удалять все записи из кэша, а только по определенному ключу
            @CacheEvict(value = "userById", key = "#id")
    })
    public void delete(Long id) {
        log.info("Delete user: {}", id);
        userRepository.deleteById(id);
    }
}
