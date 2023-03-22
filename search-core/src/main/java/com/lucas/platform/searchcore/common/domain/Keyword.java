package com.lucas.platform.searchcore.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lucas.platform.searchcore.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword extends BaseEntity implements Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int unsigned")
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(128)")
    private String text;

    @Column(columnDefinition = "int unsigned")
    private Long count;

    public Keyword(String text) {
        this.text = text;
        this.count = 1L;
    }

    public void addCount() {
        count++;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return createdAt == null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Keyword k)) return false;
        return Objects.equals(k.id, id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("""
                    Keyword {
                        id=%s,
                        text=%s,
                        count=%s,
                        createdAt=%s,
                        updatedAt=%s
                    }
                """,
                id, text, count, createdAt, updatedAt);
    }
}
