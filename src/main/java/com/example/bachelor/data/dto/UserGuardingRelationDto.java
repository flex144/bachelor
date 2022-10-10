package com.example.bachelor.data.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class UserGuardingRelationDto {

    private Long relationId;
    private Long guardDayId;
    private Long userId;
    private UserDto userDto;
    @DateTimeFormat(pattern = "hh:mm")
    private Date guardingStart;
    @DateTimeFormat(pattern = "hh:mm")
    private Date guardingEnd;
    private String userFreetext;
    private boolean booked;

    public UserGuardingRelationDto() {
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Long getGuardDayId() {
        return guardDayId;
    }

    public void setGuardDayId(Long guardDayId) {
        this.guardDayId = guardDayId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public Date getGuardingStart() {
        return guardingStart;
    }

    public void setGuardingStart(Date guardingStart) {
        this.guardingStart = guardingStart;
    }

    public Date getGuardingEnd() {
        return guardingEnd;
    }

    public void setGuardingEnd(Date guardingEnd) {
        this.guardingEnd = guardingEnd;
    }

    public String getUserFreetext() {
        return userFreetext;
    }

    public void setUserFreetext(String userFreetext) {
        this.userFreetext = userFreetext;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
