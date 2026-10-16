package com.waterreminder.entity.h2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "drink_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrinkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    @Column(length = 10, nullable = false)
    private String mode;

    @Column(name = "record_time", length = 5, nullable = false)
    private String recordTime;

    @Column(name = "record_date", length = 10, nullable = false)
    private String recordDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
