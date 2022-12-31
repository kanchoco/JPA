package com.example.basic.repository;

import com.example.basic.domain.entity.SuperCar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SuperCarRepository extends JpaRepository<SuperCar, Long> {
    public List<SuperCar> findBySuperCarReleaseDate(LocalDateTime superCarReleaseDate);
    public List<SuperCar> findBySuperCarColorContaining(String superCarColor);
    public List<SuperCar> findBySuperCarReleaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    public Page<SuperCar> findAll(Pageable pageable);
    public List<SuperCar> findTop10ByOrderBySuperCarIdDesc();

    @Modifying(clearAutomatically = true)
    @Query("update SuperCar s set s.superCarPrice = s.superCarPrice * 1.1 where s.superCarReleaseDate = :superCarReleaseDate")
    public int update(LocalDateTime superCarReleaseDate);
}

















