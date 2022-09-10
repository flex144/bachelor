package com.example.bachelor.services;

import com.example.bachelor.data.entities.JournalEntity;
import com.example.bachelor.data.entities.JournalEntryEntity;
import com.example.bachelor.repositories.JournalEntryRepository;
import com.example.bachelor.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public void saveJournal(JournalEntity journalEntity) {

        journalEntity = journalRepository.save(journalEntity);

        for (JournalEntryEntity entry : journalEntity.getJournalEntries()) {
            entry.setJournal(journalEntity);
            journalEntryRepository.save(entry);
        }

    }
}
