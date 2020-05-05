package com.spring.study.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.study.model.MongoOAuth2AccessToken;

public interface MongoOAuth2AccessTokenRepository extends MongoRepository<MongoOAuth2AccessToken, String>, MongoOAuth2AccessTokenRepositoryBase {

}
