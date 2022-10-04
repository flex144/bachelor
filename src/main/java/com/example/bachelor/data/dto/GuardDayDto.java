package com.example.bachelor.data.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuardDayDto {

    private Long guardDayId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date guardingDate;
    @DateTimeFormat(pattern = "HH:mm")
    private Date startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private Date endTime;
    @DateTimeFormat(pattern = "HH:mm")
    private Date actualEndTime;
    @DateTimeFormat(pattern = "HH:mm")
    private Date actualStartTime;
    private String waterTemp;
    private String journalDescription;
    private Long userToSave;
    private List<JournalEntryDto> journalEntries;
    private List<UserDto> allUsers;
    //Liste mit gebuchten Helfern
    private List<UserGuardingRelationDto> userGuardingRelationsBooked;
    //Liste mit anwesenden Helfern
    private List<UserGuardingRelationDto> userGuardingRelations;

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

    public String getWaterTemp() {
        return waterTemp;
    }

    public void setWaterTemp(String waterTemp) {
        this.waterTemp = waterTemp;
    }

    public String getJournalDescription() {
        return journalDescription;
    }

    public void setJournalDescription(String journalDescription) {
        this.journalDescription = journalDescription;
    }

    public Long getUserToSave() {
        return userToSave;
    }

    public void setUserToSave(Long userToSave) {
        this.userToSave = userToSave;
    }

    public List<JournalEntryDto> getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(List<JournalEntryDto> journalEntries) {
        this.journalEntries = journalEntries;
    }

    public List<UserDto> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<UserDto> allUsers) {
        this.allUsers = allUsers;
    }

    public List<UserGuardingRelationDto> getUserGuardingRelations() {
        if (userGuardingRelations == null) {
            userGuardingRelations = new ArrayList<>();
        }
        return userGuardingRelations;
    }

    public void setUserGuardingRelations(List<UserGuardingRelationDto> userGuardingRelations) {
        this.userGuardingRelations = userGuardingRelations;
    }

    public List<UserGuardingRelationDto> getUserGuardingRelationsBooked() {
        if (userGuardingRelationsBooked == null) {
            userGuardingRelationsBooked = new ArrayList<>();
        }
        return userGuardingRelationsBooked;
    }

    public void setUserGuardingRelationsBooked(List<UserGuardingRelationDto> userGuardingRelationsBooked) {
        this.userGuardingRelationsBooked = userGuardingRelationsBooked;
    }
}
