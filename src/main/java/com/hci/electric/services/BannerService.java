package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Banner;

public interface BannerService {
    public Banner save(Banner banner);
    public List<Banner> getAll();
    public boolean delete(Banner banner);
}
