package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Bill;

public interface BillService {
    public Bill save(Bill bill);
    public Bill getById(String id);
    public List<Bill> getByUserId(String userId);
}
