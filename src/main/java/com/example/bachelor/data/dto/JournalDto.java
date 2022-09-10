package com.example.bachelor.data.dto;

import java.util.List;

public class JournalDto {

    Long journalId;
    List<JournalEntryDto> journalEntries;

    public JournalDto() {
    }

    public Long getJournalId() {
        return journalId;
    }

    public void setJournalId(Long journalId) {
        this.journalId = journalId;
    }

    public List<JournalEntryDto> getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(List<JournalEntryDto> journalEntries) {
        this.journalEntries = journalEntries;
    }
}
