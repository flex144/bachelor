package com.example.bachelor.data.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class GuardDayDto {

    private Long guardDayId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date guardingDate;
    @DateTimeFormat(pattern = "hh:mm")
    private Date startTime;
    @DateTimeFormat(pattern = "hh:mm")
    private Date endTime;
    @DateTimeFormat(pattern = "hh:mm")
    private Date actualEndTime;
    @DateTimeFormat(pattern = "hh:mm")
    private Date actualStartTime;
    private List<JournalEntryDto> journalEntries;

    public GuardDayDto() {
    }

    public Long getGuardDayId() {
        return guardDayId;
    }

    public void setGuardDayId(Long guardDayId) {
        this.guardDayId = guardDayId;
    }

    public Date getGuardingDate() {
        return guardingDate;
    }

    public void setGuardingDate(Date guardingDate) {
        this.guardingDate = guardingDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Date getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public List<JournalEntryDto> getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(List<JournalEntryDto> journalEntries) {
        this.journalEntries = journalEntries;
    }
}
