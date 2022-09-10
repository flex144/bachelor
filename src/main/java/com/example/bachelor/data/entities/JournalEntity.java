package com.example.bachelor.data.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="journal")
public class JournalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_seq", allocationSize=1)
    private Long journalId;

    @OneToMany(mappedBy = "journal", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<JournalEntryEntity> journalEntries;

    public JournalEntity() {
    }

    public Long getJournalId() {
        return journalId;
    }

    public void setJournalId(Long journalId) {
        this.journalId = journalId;
    }

    public Set<JournalEntryEntity> getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(Set<JournalEntryEntity> journalEntries) {
        this.journalEntries = journalEntries;
    }
}
