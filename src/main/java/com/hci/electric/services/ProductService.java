package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Product;

public interface ProductService {
    public Product save(Product product);
    public Product getById(String id);
    public List<Product> getAll();
    public List<Product> paginate(int num, int page);
}
