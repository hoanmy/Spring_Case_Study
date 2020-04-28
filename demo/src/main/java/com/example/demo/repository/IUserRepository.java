package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.User;

public interface IUserRepository extends MongoRepository<User,String> {
	  public User findByEmailIgnoreCase(@Param("email") String email);
}
