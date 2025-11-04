package com.mose.agribora.repository;

import com.mose.agribora.entity.Sensors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<Sensors,Long> {
    List<Sensors> findByLocationOrderByTimestampDesc(String location);
}
