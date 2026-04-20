package com.kartus.api.domain.track.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "tracks")
@Getter
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, columnDefinition = "CHAR(64)", name = "track_hash")
    private String trackHash;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, name = "track_data")
    private Map<String, Object> trackData;
}
