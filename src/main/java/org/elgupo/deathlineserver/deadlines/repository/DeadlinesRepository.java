package org.elgupo.deathlineserver.deadlines.repository;

import java.time.Instant;
import java.util.List;

import org.elgupo.deathlineserver.deadlines.controller.dto.DeadlineDto;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DeadlinesRepository extends CrudRepository<DeadlineEntity, Integer> {
    public List<DeadlineDto> findByUserId(Long userId);

    public void deleteByDeadlineId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Deadlines d SET d.name = :name, d.description = :description, d.deadline = :deadline WHERE d.id = :id")
    void updateDeadline(@Param("id") Long id,
                        @Param("deadline_name") String name,
                        @Param("deadline_description") String description,
                        @Param("deadline") Instant deadline);
}
