package com.spring.study.mongo.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.study.model.MongoOAuth2RefreshToken;

public interface MongoOAuth2RefreshTokenRepository extends MongoRepository<MongoOAuth2RefreshToken, String>, MongoOAuth2RefreshTokenRepositoryBase {
}
