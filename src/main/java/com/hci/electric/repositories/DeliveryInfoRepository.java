package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.DeliveryInfo;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer> {
    
}
