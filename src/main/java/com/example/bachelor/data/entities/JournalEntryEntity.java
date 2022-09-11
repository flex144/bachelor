package com.example.bachelor.data.entities;

import com.example.bachelor.data.enums.EntryType;
import com.example.bachelor.data.enums.UserRoles;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="journal_entry")
public class JournalEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_seq", allocationSize=1)
    private Long journalEntryId;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private EntryType entryType;
    private Date creation;
    private String description;

    @ManyToOne
    @JoinColumn(name="guard_day_id", nullable = false)
    private GuardDayEntity guardDay;

    public JournalEntryEntity() {
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

    public GuardDayEntity getGuardDay() {
        return guardDay;
    }

    public void setGuardDay(GuardDayEntity guardDay) {
        this.guardDay = guardDay;
    }
}
