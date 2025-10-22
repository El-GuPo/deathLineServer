package org.elgupo.deathlineserver.deadlines.repository;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Table(name = "deadlines", schema = "public")
@Entity
public class DeadlineEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "deadline_id")
    private Long deadlineId;

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "deadline_name")
    private String name;
    @Column(name = "deadline_description")
    private String description;
    @Column(name = "deadline")
    private Instant deadline;
    @Column(name = "created_at")
    private Instant createdAt;

    public DeadlineEntity(
            Long userId,
            String name,
            String description,
            Instant deadline,
            Instant createdAt
    ) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdAt = createdAt;
    }
}
