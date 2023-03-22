package com.lucas.platform.searchapi.repository;

import com.lucas.platform.searchcore.common.domain.Keyword;
import com.lucas.platform.searchcore.common.resopnse.KeywordCount;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface KeywordJpaRepository extends JpaRepository<Keyword, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value ="3000")})
    @Query("select k from Keyword k where k.text = :text order by k.updatedAt desc limit 1")
    Optional<Keyword> findByTextForUpdate(String text);

    @Query("select k.text as text, k.count as count from Keyword k where k.updatedAt >= :begin and k.updatedAt < :end order by k.count desc")
    Slice<KeywordCount> findTopCount(Pageable pageable, ZonedDateTime begin, ZonedDateTime end);

}
