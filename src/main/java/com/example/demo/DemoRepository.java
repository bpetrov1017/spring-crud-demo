package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;
import java.time.Instant;
import java.util.List;

public interface DemoRepository extends JpaRepository<DemoEntity, String> {

    default List<DemoEntity> findAllSorted() {
        return findAll(Sort.by(Sort.Direction.DESC, "modifiedAt"));
    }

    @Query("""
        SELECT e FROM DemoEntity e
        WHERE 
            (e.modifiedAt < :cursorTime)
            OR (e.modifiedAt = :cursorTime AND e.id < :cursorId)
        ORDER BY e.modifiedAt DESC, e.id DESC
    """)
    List<DemoEntity> findNextPage(@Param("cursorTime") Instant cursorTime, @Param("cursorId") String cursorId);
}