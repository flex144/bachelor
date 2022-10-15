package com.example.bachelor.repositories;

import com.example.bachelor.data.entities.GuardDayEntity;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.data.entities.UserGuardingRelationEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserGuardingRelationRepository extends CrudRepository<UserGuardingRelationEntity, Long> {
    Iterable<UserGuardingRelationEntity> findUserGuardingRelationEntitiesByGuardDayId(Long guardDayId);
    Iterable<UserGuardingRelationEntity> findUserGuardingRelationEntitiesByUserId(Long userId);
}
