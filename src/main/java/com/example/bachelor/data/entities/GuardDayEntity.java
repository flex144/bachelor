package com.example.bachelor.data.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="guard_day")
public class GuardDayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_seq", allocationSize=1)
    private Long guardDayId;

    @Temporal(TemporalType.DATE)
    private Date guardingDate;

    @Temporal(TemporalType.TIME)
    private Date startTime;

    @Temporal(TemporalType.TIME)
    private Date endTime;

    @Temporal(TemporalType.TIME)
    private Date actualStartTime;

    @Temporal(TemporalType.TIME)
    private Date actualEndTime;

    @OneToMany(mappedBy = "guardDay", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<JournalEntryEntity> journalEntries;

    public GuardDayEntity() {
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

    public Date getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Set<JournalEntryEntity> getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(Set<JournalEntryEntity> journalEntries) {
        this.journalEntries = journalEntries;
    }
}
