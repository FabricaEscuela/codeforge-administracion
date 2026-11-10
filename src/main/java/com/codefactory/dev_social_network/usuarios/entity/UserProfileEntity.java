package com.codefactory.dev_social_network.usuarios.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.Year;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "perfiles_usuario")
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    private Usuario userId;

    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "nivel_completado", precision = 5, scale = 2)
    private BigDecimal completedProfile;

    @Column(name = "enlace_portafolio", length = 255)
    private String porfolioLink;

    // La BD guarda el año en que empezó a programar; los años de experiencia se calculan
    @Column(name = "anio_inicio_dev")
    private Integer devStartYear;

    @Column(name = "nivel", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ExperienceLevel seniorityLevel;

    protected UserProfileEntity() {}

    public UserProfileEntity(Usuario userId) {
        this.userId = userId;
        this.createdAt = OffsetDateTime.now();
        this.completedProfile = BigDecimal.ZERO;
        this.seniorityLevel = ExperienceLevel.ENTRY;
    }

    public UUID getId() {
        return id;
    }

    public Usuario getUserId() {
        return userId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public BigDecimal getCompletedProfile() {
        return completedProfile;
    }

    public void setCompletedProfile(BigDecimal completedProfile) {
        this.completedProfile = completedProfile;
    }

    public String getPorfolioLink() {
        return porfolioLink;
    }

    public void setPorfolioLink(String porfolioLink) {
        this.porfolioLink = porfolioLink;
    }

    public Integer getDevStartYear() {
        return devStartYear;
    }

    public void setDevStartYear(Integer devStartYear) {
        this.devStartYear = devStartYear;
    }

    /** Años de experiencia calculados a partir del año de inicio guardado en la BD. */
    public Integer getYearsOfExperience() {
        return devStartYear == null ? null : Year.now().getValue() - devStartYear;
    }

    public void setYearsOfExperience(Integer years) {
        this.devStartYear = years == null ? null : Year.now().getValue() - years;
    }

    public ExperienceLevel getSeniorityLevel() {
        return seniorityLevel;
    }

    public void setSeniorityLevel(ExperienceLevel seniorityLevel) {
        this.seniorityLevel = seniorityLevel;
    }
}