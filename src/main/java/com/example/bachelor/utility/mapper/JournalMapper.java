package com.example.bachelor.utility.mapper;

import com.example.bachelor.data.dto.JournalEntryDto;
import com.example.bachelor.data.entities.JournalEntryEntity;
import com.example.bachelor.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JournalMapper extends BaseMapper {

    @Autowired
    UserService userService;

    public List<JournalEntryDto> mapJournalEntryEntitiesToDto(Set<JournalEntryEntity> journalEntryEntities) {

        List<JournalEntryDto> res = new ArrayList<>();

        if (journalEntryEntities != null) {
            for (JournalEntryEntity journalEntryEntity : journalEntryEntities) {
                JournalEntryDto journalEntryDto = modelMapper.map(journalEntryEntity, JournalEntryDto.class);
                if (journalEntryEntity.getUserId() != null) {
                    journalEntryDto.setUserDto(userService.readUserDtoById(journalEntryEntity.getUserId()));
                }

                res.add(journalEntryDto);
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
