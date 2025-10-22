package org.elgupo.deathlineserver.deadlines.repository;

import java.time.Instant;
import java.util.List;

import org.elgupo.deathlineserver.deadlines.controller.dto.DeadlineDto;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DeadlinesRepository extends CrudRepository<DeadlineEntity, Integer> {
    public List<DeadlineDto> findByUserId(Long userId);

    public void deleteByDeadlineId(Long userId);

    @Query("SELECT * FROM deadlines WHERE deadline BETWEEN :start AND :end")
    public List<DeadlineEntity> findByDeadlineBetween(@Param("start") Instant start, @Param("end") Instant end);

}
