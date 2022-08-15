package com.example.bachelor.repositories;

import com.example.bachelor.data.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Iterable<UserEntity> findUserByEmail(String email);
}
