package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Distributor;

public interface DistributorService {
    public Distributor save(Distributor distributor);
    public Distributor getByUserId(String userId);
    public Distributor getById(String id);
    public List<Distributor> getAll();
}
