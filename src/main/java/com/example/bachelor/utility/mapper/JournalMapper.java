package com.example.bachelor.utility.mapper;

import com.example.bachelor.data.dto.JournalEntryDto;
import com.example.bachelor.data.entities.JournalEntryEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JournalMapper extends BaseMapper {

    public List<JournalEntryDto> mapJournalEntryEntitiesToDto(Set<JournalEntryEntity> journalEntryEntities) {

        List<JournalEntryDto> res = new ArrayList<>();

        if (journalEntryEntities != null) {
            for (JournalEntryEntity journalEntryEntity : journalEntryEntities) {
                res.add(modelMapper.map(journalEntryEntity, JournalEntryDto.class));
            }
        }
        return res;
    }

    public Set<JournalEntryEntity> mapJournalEntryDtosToEntity(List<JournalEntryDto> journalEntryDtos) {

        Set<JournalEntryEntity> res = new HashSet<>();

        if (journalEntryDtos != null) {
            for (JournalEntryDto journalEntryDto : journalEntryDtos) {
                res.add(modelMapper.map(journalEntryDto, JournalEntryEntity.class));
            }
        }

        return res;
    }
}
