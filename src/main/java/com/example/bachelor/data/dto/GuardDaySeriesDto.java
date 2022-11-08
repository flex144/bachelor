package com.example.bachelor.data.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuardDaySeriesDto {

    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @DateTimeFormat(pattern = "HH:mm")
    private Date startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private Date endTime;

    public GuardDaySeriesDto() {
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public boolean noDaySet() {
        return !(monday || tuesday || wednesday || thursday || friday || saturday || sunday);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public List<DayOfWeek> getDaysOfWeek() {
        List<DayOfWeek> daysOfWeek = new ArrayList<>();
        if (isMonday()) {
            daysOfWeek.add(DayOfWeek.MONDAY);
        }
        if (isTuesday()) {
            daysOfWeek.add(DayOfWeek.TUESDAY);
        }
        if (isWednesday()) {
            daysOfWeek.add(DayOfWeek.WEDNESDAY);
        }
        if (isThursday()) {
            daysOfWeek.add(DayOfWeek.THURSDAY);
        }
        if (isFriday()) {
            daysOfWeek.add(DayOfWeek.FRIDAY);
        }
        if (isSaturday()) {
            daysOfWeek.add(DayOfWeek.SATURDAY);
        }
        if (isSunday()) {
            daysOfWeek.add(DayOfWeek.SUNDAY);
        }

        return daysOfWeek;
    }
}
