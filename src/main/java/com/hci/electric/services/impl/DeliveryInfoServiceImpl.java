package com.hci.electric.services.impl;

import org.springframework.stereotype.Service;

import com.hci.electric.models.DeliveryInfo;
import com.hci.electric.repositories.DeliveryInfoRepository;
import com.hci.electric.services.DeliveryInfoService;


@Service
public class DeliveryInfoServiceImpl implements DeliveryInfoService {
    private final DeliveryInfoRepository deliveryInfoRepository;

    public DeliveryInfoServiceImpl(DeliveryInfoRepository deliveryInfoRepository){
        this.deliveryInfoRepository = deliveryInfoRepository;
    }

    @Override
    public DeliveryInfo save(DeliveryInfo info){
        try{
            return this.deliveryInfoRepository.save(info);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

}
