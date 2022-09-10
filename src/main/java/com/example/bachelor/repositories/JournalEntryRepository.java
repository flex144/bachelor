package com.example.bachelor.repositories;

import com.example.bachelor.data.entities.JournalEntity;
import com.example.bachelor.data.entities.JournalEntryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JournalEntryRepository extends CrudRepository<JournalEntryEntity, Long> {
    List<JournalEntryEntity> findByJournal(JournalEntity journalEntity, Sort sort);
}
