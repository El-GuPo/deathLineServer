package org.elgupo.deathlineserver.deadlines.controller;

import java.time.Instant;
import java.util.List;

import org.elgupo.deathlineserver.deadlines.controller.dto.DeadlineDto;
import org.elgupo.deathlineserver.deadlines.services.DeadlinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeadlinesController {

    @Autowired
    private final DeadlinesService deadlineService;

    public DeadlinesController(DeadlinesService deadlineService) {
        this.deadlineService = deadlineService;
    }

    @GetMapping("/deadlines/get_deadlines_for_user")
    public List<DeadlineDto> getDeadlinesForUser(@RequestParam("userId") Long userId,
                                              @RequestParam(required = false) Instant from,
                                              @RequestParam(required = false) Instant to) {
        return deadlineService.getDeadlinesForUser(userId, from, to);
    }

    @PostMapping("/deadlines/create_deadline_for_user")
    public void createDeadlineForUser(@RequestParam("userId") Long userId,
                                      @RequestParam("deadline") DeadlineDto deadline) {
        deadlineService.createDeadlineForUser(userId, deadline);
    }

    @PostMapping("/deadlines/delete_deadline_for_user")
    public void deleteDeadlineForUser(@RequestParam("deadlineId") Long deadlineId) {
        deadlineService.deleteDeadlineForUser(deadlineId);
    }

    @PostMapping("/deadlines/update_deadline_for_user")
    public void updateDeadlineForUser(@RequestParam("deadline") DeadlineDto deadline) {
        deadlineService.updateDeadlineForUser(deadline);
    }
}
