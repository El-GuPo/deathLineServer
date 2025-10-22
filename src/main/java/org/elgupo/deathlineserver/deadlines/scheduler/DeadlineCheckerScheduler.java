package org.elgupo.deathlineserver.deadlines.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elgupo.deathlineserver.deadlines.repository.DeadlinesRepository;
import org.elgupo.deathlineserver.deadlines.repository.DeadlineEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeadlineCheckerScheduler {

    private final DeadlinesRepository deadlinesRepository;

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void checkDeadlines() {
        log.info("Checking for upcoming deadlines");

        Instant now = Instant.now();
        Instant threshold = now.plusSeconds(30 * 60);

        List<DeadlineEntity> upcomingDeadlines = deadlinesRepository
                .findByDeadlineBetween(now, threshold);

        for (DeadlineEntity deadline : upcomingDeadlines) {
            handleUpcomingDeadline(deadline);
        }
    }

    private void handleUpcomingDeadline(DeadlineEntity deadline) {
        // TODO: Добавить логику уведомления пользователя
    }
}
