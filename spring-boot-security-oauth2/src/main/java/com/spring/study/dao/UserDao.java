package com.spring.study.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.study.model.User;

//@Repository
//public interface UserDao extends CrudRepository<User, Long> {
//    User findByUsername(String username);
//}


public interface UserDao extends MongoRepository<User,String> {
	  User findByUsername(@Param("username") String username);
	  
	  @Query(value = "{}", fields="{ password: 0 }")
	  List<User> findAll();
	  
//	  User findByEmailIgnoreCase(@Param("username") String username);
}
