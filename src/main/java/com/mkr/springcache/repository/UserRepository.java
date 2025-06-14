package com.mkr.springcache.repository;

import com.mkr.springcache.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
