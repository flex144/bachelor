package com.example.bachelor.data.dto;

import java.util.List;

public class UserStatisticsDto {

    private Long userId;
    private List<UserGuardingRelationDto> userGuardingRelations;
    private Long hours;
    private Long minutes;
    private Long seconds;
    private Long totalSeconds;
    private int daysPresent;

    public UserStatisticsDto() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<UserGuardingRelationDto> getUserGuardingRelations() {
        return userGuardingRelations;
    }

    public void setUserGuardingRelations(List<UserGuardingRelationDto> userGuardingRelations) {
        this.userGuardingRelations = userGuardingRelations;
    }

    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }

    public Long getMinutes() {
        return minutes;
    }

    public void setMinutes(Long minutes) {
        this.minutes = minutes;
    }

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    public int getDaysPresent() {
        return daysPresent;
    }

    public void setDaysPresent(int daysPresent) {
        this.daysPresent = daysPresent;
    }

    public Long getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(Long totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (hours == 0 && minutes == 0 && seconds == 0) {
            sb.append(" - ");
        } else {
            sb.append(hours + " ");
            sb.append("Stunden, ");
            sb.append(minutes + " ");
            sb.append("Minuten, ");
            sb.append(seconds + " Sekunden");
        }
        return sb.toString();
    }
}
