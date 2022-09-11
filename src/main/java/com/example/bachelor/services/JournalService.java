package com.example.bachelor.services;

import com.example.bachelor.data.dto.JournalEntryDto;
import com.example.bachelor.data.entities.JournalEntryEntity;
import com.example.bachelor.repositories.JournalEntryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class JournalService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void saveJournal(Set<JournalEntryEntity> journalEntities) {

        if (journalEntities != null) {
            journalEntryRepository.saveAll(journalEntities);
        }

    }

    public void saveJournalEntry(JournalEntryEntity journalEntryEntity) {
        journalEntryRepository.save(journalEntryEntity);
    }

    public List<JournalEntryDto> mapJournalEntriesToDto(Set<JournalEntryEntity> journalEntryEntities) {

        List<JournalEntryDto> res = new ArrayList<>();

        if (journalEntryEntities != null) {
            for (JournalEntryEntity journalEntryEntity : journalEntryEntities) {
                res.add(modelMapper.map(journalEntryEntity, JournalEntryDto.class));
            }
        }
        return res;
    }
}
