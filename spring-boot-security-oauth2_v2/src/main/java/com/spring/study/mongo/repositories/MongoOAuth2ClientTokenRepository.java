package com.spring.study.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.study.model.MongoOAuth2ClientToken;

public interface MongoOAuth2ClientTokenRepository
		extends MongoRepository<MongoOAuth2ClientToken, String>, MongoOAuth2ClientTokenRepositoryBase {
}
