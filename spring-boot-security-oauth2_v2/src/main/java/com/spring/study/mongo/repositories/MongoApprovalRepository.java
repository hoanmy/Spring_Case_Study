package com.spring.study.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.study.model.MongoApproval;

public interface MongoApprovalRepository extends MongoRepository<MongoApproval, String>, MongoApprovalRepositoryBase {
}
