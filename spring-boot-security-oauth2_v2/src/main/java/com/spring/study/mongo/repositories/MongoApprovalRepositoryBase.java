package com.spring.study.mongo.repositories;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.spring.study.model.MongoApproval;

public interface MongoApprovalRepositoryBase {
    boolean updateOrCreate(Collection<MongoApproval> mongoApprovals);

    boolean updateExpiresAt(LocalDateTime now, MongoApproval mongoApproval);

    boolean deleteByUserIdAndClientIdAndScope(MongoApproval mongoApproval);

    List<MongoApproval> findByUserIdAndClientId(String userId, String clientId);
}
