package com.example.bachelor.repositories;

import com.example.bachelor.data.entities.JournalEntity;
import org.springframework.data.repository.CrudRepository;

public interface JournalRepository extends CrudRepository<JournalEntity, Long> {
}
