package com.hci.electric.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.DeliveryInfo;
import com.hci.electric.utils.queries.DeliveryInfoQuery;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer> {
    @Query(value = DeliveryInfoQuery.queryGetByUser, nativeQuery = true)
    public List<DeliveryInfo> getByUserId(String userId);
}
