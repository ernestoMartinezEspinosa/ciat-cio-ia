package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sector_strength")
public class SectorStrengthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100)
    private String sector;

    @Column(name = "strength_score", precision = 10, scale = 4)
    private BigDecimal strengthScore;

    @Column(name = "score_date", nullable = false)
    private LocalDate scoreDate;

    public SectorStrengthEntity() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public BigDecimal getStrengthScore() { return strengthScore; }
    public void setStrengthScore(BigDecimal strengthScore) { this.strengthScore = strengthScore; }

    public LocalDate getScoreDate() { return scoreDate; }
    public void setScoreDate(LocalDate scoreDate) { this.scoreDate = scoreDate; }
}