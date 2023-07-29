package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Product;
import com.hci.electric.repositories.ProductRepository;
import com.hci.electric.services.ProductService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Libraries;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product){
        try{
            product.setId(Libraries.generateId(Constants.lengthId));
        
            product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            product.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.productRepository.save(product);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Product getById(String id){
        try{
            Optional<Product> product = this.productRepository.findById(id);
            if (product.isPresent() == false){
                return null;
            }

            return product.get();
        }  
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getAll(){
        try{    
            return this.productRepository.findAll();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> paginate(int num, int page){
        try{
            Optional<List<Product>> products = this.productRepository.paginate(num, (page-1)*num);
            if (products.isPresent() == false){
                return null;
            }

            return products.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Product edit(Product product){
        try{
            product.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.productRepository.save(product);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public String delete(String id) {
        try {
            this.productRepository.deleteById(id);

            return id;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getByDistributorId(String distributorId) {
        try {
            return this.productRepository.findByDistributorId(distributorId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
