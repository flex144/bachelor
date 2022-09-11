package com.example.bachelor.repositories;

import com.example.bachelor.data.entities.GuardDayEntity;
import com.example.bachelor.data.entities.JournalEntryEntity;
import com.example.bachelor.data.entities.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JournalEntryRepository extends CrudRepository<JournalEntryEntity, Long> {
    Iterable<JournalEntryEntity> findJournalEntryEntitiesByGuardDay(GuardDayEntity guardDayEntity);
}
