package com.hci.electric.services;

import java.util.List;
import java.util.Optional;

import com.hci.electric.models.DeliveryInfo;

public interface DeliveryInfoService {
    public DeliveryInfo save(DeliveryInfo info);
    public List<DeliveryInfo> getByUserId(String userId);
    public Boolean delete(int id);
    public Optional<DeliveryInfo> getById(int id);
}
