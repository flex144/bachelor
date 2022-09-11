package com.example.bachelor.data.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class GuardDayDto {

    private Long guardDayId;
    private LocalDate guardingDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime actualEndTime;
    private LocalDateTime actualStartTime;
    private List<JournalEntryDto> journalEntries;

    public GuardDayDto() {
    }

    public Long getGuardDayId() {
        return guardDayId;
    }

    public void setGuardDayId(Long guardDayId) {
        this.guardDayId = guardDayId;
    }

    public LocalDate getGuardingDate() {
        return guardingDate;
    }

    public void setGuardingDate(LocalDate guardingDate) {
        this.guardingDate = guardingDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public List<JournalEntryDto> getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(List<JournalEntryDto> journalEntries) {
        this.journalEntries = journalEntries;
    }
}
