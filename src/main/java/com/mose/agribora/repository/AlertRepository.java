package com.mose.agribora.repository;

import com.mose.agribora.entity.Alerts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alerts, Long> {
    List<Alerts> findByRecipientEmail(String email);
    List<Alerts> findByLocation(String location);
}
