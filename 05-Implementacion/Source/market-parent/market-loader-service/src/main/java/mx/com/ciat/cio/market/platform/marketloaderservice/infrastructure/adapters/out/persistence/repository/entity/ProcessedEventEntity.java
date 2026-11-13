package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
        name = "processed_events",
        indexes = {
                @Index(
                        name = "idx_processed_events_correlation",
                        columnList = "correlation_id"
                )
        }
)
public class ProcessedEventEntity {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR) // O usar SqlTypes.VARCHAR
    @Column(name = "event_id", nullable = false, length = 36)
    private UUID eventId;

    @JdbcTypeCode(SqlTypes.CHAR) // O usar SqlTypes.VARCHAR
    @Column(name = "correlation_id", length = 36)
    private UUID correlationId;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    protected ProcessedEventEntity() {
    }

    public ProcessedEventEntity(UUID eventId, UUID correlationId) {
        this.eventId = eventId;
        this.correlationId = correlationId;
        this.processedAt = Instant.now();
    }

    @PrePersist
    public void prePersist() {

        if (processedAt == null) {
            processedAt = Instant.now();
        }

    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

}