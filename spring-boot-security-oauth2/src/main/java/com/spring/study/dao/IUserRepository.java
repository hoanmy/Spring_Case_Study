package com.spring.study.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.spring.study.model.User;

public interface IUserRepository extends MongoRepository<User,String> {
	  public User findByEmailIgnoreCase(@Param("username") String username);
}
