package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Bill;

public interface BillService {
    public Bill save(Bill bill);
    public Bill getById(String id);
    public Bill edit(Bill bill);
    public List<Bill> getByUserId(String userId);
    public List<Bill> getByUserIdAndStatus(String userId, String status);
    public List<Bill> paginateBillsByUserId(String userId, int page, int num);
    public List<Bill> paginateBillsByUserIdAndStatus(String userId, String status, int page, int num);
    public List<Bill> getAll();
    public List<Bill> paginateBills(int page, int num);
    public List<Bill> getBillsByPaymentTypeAndStatus(String paymentType, String status);
}
