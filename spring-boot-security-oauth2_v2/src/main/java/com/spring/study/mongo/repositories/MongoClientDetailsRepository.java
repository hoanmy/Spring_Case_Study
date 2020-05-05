package com.spring.study.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.study.model.MongoClientDetails;

public interface MongoClientDetailsRepository extends MongoRepository<MongoClientDetails, String>, MongoClientDetailsRepositoryBase {
}
