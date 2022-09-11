package com.example.bachelor.repositories;

import com.example.bachelor.data.entities.GuardDayEntity;
import com.example.bachelor.data.entities.JournalEntryEntity;
import org.springframework.data.repository.CrudRepository;

public interface GuardDayRepository extends CrudRepository<GuardDayEntity, Long> {
}
