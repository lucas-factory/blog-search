package com.lucas.platform.searchcore.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
public abstract class BaseEntity {

    @Column(nullable = false, updatable = false, columnDefinition = "timestamp")
    protected ZonedDateTime createdAt;

    @Column(nullable = false, columnDefinition = "timestamp")
    protected ZonedDateTime updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = ZonedDateTime.now(ZoneId.systemDefault());
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = ZonedDateTime.now(ZoneId.systemDefault());
    }

    @Override
    public String toString() {
        return String.format("""
                    BaseEntity {
                        createdAt=%s,
                        updatedAt=%s
                    }
                """, createdAt, updatedAt);
    }

}
