package com.hci.electric.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Distributor;
import com.hci.electric.utils.queries.DistributorQuery;

public interface DistributorRepository extends JpaRepository<Distributor, String> {
    @Query(value = DistributorQuery.queryGetByUserId, nativeQuery = true)
    public Optional<Distributor> getByUserId(String userId);
}
