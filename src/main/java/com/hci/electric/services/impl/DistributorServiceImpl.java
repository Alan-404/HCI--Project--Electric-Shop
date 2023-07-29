package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Distributor;
import com.hci.electric.repositories.DistributorRepository;
import com.hci.electric.services.DistributorService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Libraries;


@Service
public class DistributorServiceImpl implements DistributorService {
    private final DistributorRepository distributorRepository;

    public DistributorServiceImpl(DistributorRepository distributorRepository){
        this.distributorRepository = distributorRepository;
    }

    @Override
    public Distributor save(Distributor distributor){
        try{
            distributor.setId(Libraries.generateId(Constants.lengthId));
            distributor.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            distributor.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.distributorRepository.save(distributor);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Distributor getByUserId(String userId){
        try{
            Optional<Distributor> distributor = this.distributorRepository.getByUserId(userId);
            if (distributor.isPresent() == false){
                return null;
            }

            return distributor.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Distributor getById(String id){
        try{
            Optional<Distributor> distributor = this.distributorRepository.findById(id);
            if (distributor.isPresent() == false){
                return null;
            }

            return distributor.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Distributor> getAll() {
        try {
            return this.distributorRepository.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public String delete(String id) {
        try {
            this.distributorRepository.deleteById(id);
            return id;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
