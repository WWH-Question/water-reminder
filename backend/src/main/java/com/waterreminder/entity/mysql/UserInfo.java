package com.waterreminder.entity.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false)
    @Builder.Default
    private Integer height = 170;

    @Column(nullable = false)
    @Builder.Default
    private Integer weight = 65;

    @Column(nullable = false)
    @Builder.Default
    private Integer coefficient = 35;

    @Column(name = "work_start", length = 5, nullable = false)
    @Builder.Default
    private String workStart = "09:00";

    @Column(name = "work_end", length = 5, nullable = false)
    @Builder.Default
    private String workEnd = "18:00";

    @Column(name = "sleep_time", length = 5, nullable = false)
    @Builder.Default
    private String sleepTime = "23:00";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
