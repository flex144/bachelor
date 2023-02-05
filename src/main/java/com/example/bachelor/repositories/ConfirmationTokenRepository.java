package com.example.bachelor.repositories;

import com.example.bachelor.data.entities.ConfirmationTokenEntity;
import com.example.bachelor.data.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository  extends CrudRepository<ConfirmationTokenEntity, Long> {
    Iterable<ConfirmationTokenEntity> findUserByConfirmationToken(String token);
}
