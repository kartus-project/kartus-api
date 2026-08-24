package com.kartus.api.domain.track.repository;

import com.kartus.api.domain.track.dto.query.TrackIdHashDTO;
import com.kartus.api.domain.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {
    @Query("SELECT new com.kartus.api.domain.track.dto.query.TrackIdHashDTO(t.id, t.trackHash) FROM Track t")
    List<TrackIdHashDTO> findAllIdAndHash();

    Optional<Track> findFirstByOrderByIdAsc();
}
