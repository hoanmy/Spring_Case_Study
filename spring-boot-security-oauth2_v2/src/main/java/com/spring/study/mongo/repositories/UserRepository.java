package com.spring.study.mongo.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.study.model.User;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryBase {

    void deleteByUsername(String username);

    Optional<User> findByUsername(String username);

}
