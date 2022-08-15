package com.example.bachelor.data.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="journal")
public class JournalEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long journalId;

    @OneToMany(mappedBy = "journal", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<JournalEntryEntity> journalEntries;
}
