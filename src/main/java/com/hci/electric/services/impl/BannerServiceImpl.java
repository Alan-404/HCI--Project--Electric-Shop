package com.hci.electric.services.impl;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Banner;
import com.hci.electric.repositories.BannerRepository;
import com.hci.electric.services.BannerService;

@Service
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;

    public BannerServiceImpl(BannerRepository bannerRepository){
        this.bannerRepository = bannerRepository;
    }

    @Override
    public Banner save(Banner banner){
        try{
            return this.bannerRepository.save(banner);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
