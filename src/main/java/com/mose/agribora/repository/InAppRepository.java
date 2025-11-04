package com.mose.agribora.repository;

import com.mose.agribora.entity.InAppAlert;
import com.mose.agribora.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InAppRepository extends JpaRepository<InAppAlert, Long> {
    List<InAppAlert> findByProfile(Profile profile);
}
