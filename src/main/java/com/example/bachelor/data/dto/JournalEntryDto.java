package com.example.bachelor.data.dto;

import com.example.bachelor.data.enums.EntryType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

public class JournalEntryDto {

    Long journalId;
    private Long journalEntryId;
    private Long userId;
    private EntryType entryType;
    private Date creation;
    private String description;

    public JournalEntryDto() {
    }

    public Long getJournalId() {
        return journalId;
    }

    public void setJournalId(Long journalId) {
        this.journalId = journalId;
    }

    public Long getJournalEntryId() {
        return journalEntryId;
    }

    public void setJournalEntryId(Long journalEntryId) {
        this.journalEntryId = journalEntryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}