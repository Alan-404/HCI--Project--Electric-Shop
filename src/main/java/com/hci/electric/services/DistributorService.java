package com.hci.electric.services;

import com.hci.electric.models.Distributor;

public interface DistributorService {
    public Distributor save(Distributor distributor);
    public Distributor getByUserId(String userId);
}
