package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Bill;
import com.hci.electric.utils.queries.BillQuery;

public interface BillRepository extends JpaRepository<Bill, String> {
    @Query(value = BillQuery.queryGetBiilsByUser, nativeQuery = true)
    public Optional<List<Bill>> getByUserId(String userId);

    @Query(value = BillQuery.queryGetBillsByUserAndStatus, nativeQuery = true)
    public Optional<List<Bill>> getByUserAndStatus(String userId, String status);

    @Query(value = BillQuery.paginateBillsByUser, nativeQuery = true)
    public Optional<List<Bill>> paginateBillsByUser(String userId, int limit, int offset);

    @Query(value = BillQuery.paginateBills, nativeQuery = true)
    Optional<List<Bill>> paginateBills(int limit, int offset);
}
