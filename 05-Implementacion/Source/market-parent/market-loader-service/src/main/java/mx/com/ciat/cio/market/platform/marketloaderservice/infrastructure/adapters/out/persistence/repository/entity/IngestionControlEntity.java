package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "ingestion_control",
        indexes = {

                @Index(
                        name = "idx_ingestion_source",
                        columnList = "source_name"
                ),

                @Index(
                        name = "idx_ingestion_status",
                        columnList = "status"
                ),

                @Index(
                        name = "idx_ingestion_instrument",
                        columnList = "instrument_id"
                )

        }
)

@Getter
@Setter
public class IngestionControlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_name", nullable = false, length = 100)
    private String sourceName;

    @Column(name = "instrument_id")
    private Long instrumentId;

    @Column(length = 20)
    private String ticker;

    @Column(length = 10)
    private String timeframe;

    @Column(name = "requested_from")
    private Instant requestedFrom;

    @Column(name = "requested_to")
    private Instant requestedTo;

    @Column(name = "execution_start", nullable = false)
    private Instant executionStart;

    @Column(name = "execution_end")
    private Instant executionEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IngestionStatus status;

    @Column(name = "downloaded_records")
    private Long downloadedRecords;

    @Column(name = "persisted_records")
    private Long persistedRecords;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Lob
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    public IngestionControlEntity() {
    }

    @PrePersist
    public void prePersist() {

        if (executionStart == null) {
            executionStart = Instant.now();
        }

        if (status == null) {
            status = IngestionStatus.STARTED;
        }

    }

    public void finishSuccess(long downloaded, long persisted) {

        this.executionEnd = Instant.now();

        this.downloadedRecords = downloaded;

        this.persistedRecords = persisted;

        this.executionTimeMs =
                executionEnd.toEpochMilli() - executionStart.toEpochMilli();

        this.status = IngestionStatus.COMPLETED;
    }

    public void finishError(Exception ex) {

        this.executionEnd = Instant.now();

        this.executionTimeMs =
                executionEnd.toEpochMilli() - executionStart.toEpochMilli();

        this.status = IngestionStatus.FAILED;

        this.errorMessage = ex.getMessage();
    }

}