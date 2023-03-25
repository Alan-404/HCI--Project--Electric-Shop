package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Bill;
import com.hci.electric.repositories.BillRepository;
import com.hci.electric.services.BillService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Libraries;



@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository){
        this.billRepository = billRepository;
    }

    @Override
    public Bill save(Bill bill){
        try{
            bill.setId(Libraries.generateId(Constants.lengthId));
            bill.setOrderTime(new Timestamp(System.currentTimeMillis()));
            return this.billRepository.save(bill);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Bill getById(String id){
        try{
            Optional<Bill> bill = this.billRepository.findById(id);
            if (bill.isPresent() == false){
                return null;
            }
            return bill.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Bill> getByUserId(String userId){
        try{
            Optional<List<Bill>> bills = this.billRepository.getByUserId(userId);
            if (bills.isPresent() == false){
                return null;
            }
            return bills.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Bill> getByUserIdAndStatus(String userId, String status){
        try{
            Optional<List<Bill>> bills = this.billRepository.getByUserAndStatus(userId, status);
            if (bills.isPresent() == false){
                return new ArrayList<>();
            }
            return bills.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
