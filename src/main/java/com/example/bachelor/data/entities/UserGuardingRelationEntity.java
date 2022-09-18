package com.example.bachelor.data.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="user_guarding_relation")
public class UserGuardingRelationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_seq", allocationSize=1)
    private Long relationId;

    private Long guardDayId;

    private Long userId;

    @Temporal(TemporalType.TIME)
    private Date guardingStart;

    @Temporal(TemporalType.TIME)
    private Date guardingEnd;

    private String userFreetext;

    @Column(nullable=false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean booked;

    public UserGuardingRelationEntity() {
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
