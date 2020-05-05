package com.spring.study.mongo.repositories;

import com.spring.study.model.MongoClientDetails;

public interface MongoClientDetailsRepositoryBase {
    boolean deleteByClientId(String clientId);

    boolean update(MongoClientDetails mongoClientDetails);

    boolean updateClientSecret(String clientId, String newSecret);

    MongoClientDetails findByClientId(String clientId);
}
