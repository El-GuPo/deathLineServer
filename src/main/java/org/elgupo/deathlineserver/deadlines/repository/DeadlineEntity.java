package org.elgupo.deathlineserver.deadlines.repository;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@RequiredArgsConstructor
public class DeadlineEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonProperty("deadline_id")
    private Long deadlineId;
    @JsonProperty("user_id")
    private final Long userId;
    @JsonProperty("deadline_name")
    private final String name;
    @JsonProperty("deadline_description")
    private final String description;
    @JsonProperty("deadline")
    private final Instant deadline;
    @JsonProperty("created_at")
    private final Instant createdAt;
}
