package org.elgupo.deathlineserver.deadlines.services;

import java.time.Instant;
import java.util.List;

import org.elgupo.deathlineserver.deadlines.controller.dto.DeadlineDto;
import org.elgupo.deathlineserver.deadlines.repository.DeadlineEntity;
import org.elgupo.deathlineserver.deadlines.repository.DeadlinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeadlinesService {
    private final DeadlinesRepository deadlinesRepository;

    @Autowired
    public DeadlinesService(DeadlinesRepository deadlinesRepository) {
        this.deadlinesRepository = deadlinesRepository;
    }
    public List<DeadlineDto> getDeadlinesForUser(Long userId, Instant from, Instant to) {
        if (from == null && to == null){
            return deadlinesRepository.findByUserId(userId);
        }
        else if (from == null) {
            return deadlinesRepository.findByUserId(userId).stream()
                    .filter(deadline -> deadline.getDeadline().isBefore(to))
                    .toList();
        }
        else if (to == null) {
            return deadlinesRepository.findByUserId(userId).stream()
                    .filter(deadline -> deadline.getDeadline().isAfter(from))
                    .toList();
        }
        return deadlinesRepository.findByUserId(userId).stream()
                .filter(deadline -> deadline.getDeadline().isAfter(from) && deadline.getDeadline().isBefore(to))
                .toList();
    }
    public void createDeadlineForUser(Long userId, DeadlineDto deadline) {
        deadlinesRepository.save(new DeadlineEntity(userId, deadline.getName(), deadline.getDescription(), deadline.getDeadline(), Instant.now()));
    }
    public void deleteDeadlineForUser(Long deadlineId) {
        deadlinesRepository.deleteByDeadlineId(deadlineId);
    }
}
