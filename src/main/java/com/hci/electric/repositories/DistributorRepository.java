package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.Distributor;

public interface DistributorRepository extends JpaRepository<Distributor, String> {
    
}
