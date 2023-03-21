package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.Banner;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
    
}
