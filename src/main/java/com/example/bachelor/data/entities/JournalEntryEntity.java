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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "journalId", nullable = false)
    private JournalEntity journal;
}
