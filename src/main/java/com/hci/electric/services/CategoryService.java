package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Category;

public interface CategoryService {
    public Category save(Category category);
    public Category getById(String id);
    public Category edit(Category category);
    public List<Category> getAll();
    public boolean checkListCategories(List<String> categories);
    public List<Category> paginate(int num, int page);
    public String delete(String id);
}
