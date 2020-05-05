package com.spring.study.service;

import java.util.List;

import com.spring.study.model.User;

public interface UserService {

    User save(User user);
    List<User> findAll();
    void delete(long id);
}
