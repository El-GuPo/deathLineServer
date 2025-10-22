package org.elgupo.deathlineserver.deadlines.controller.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeadlineDto {
    @JsonProperty("deadline_id")
    private Long deadlineId;
    @JsonProperty("deadline_name")
    private String name;
    @JsonProperty("deadline_description")
    private String description;
    @JsonProperty("deadline")
    private Instant deadline;
}
