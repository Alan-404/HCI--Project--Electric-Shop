package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Category;
import com.hci.electric.repositories.CategoryRepository;
import com.hci.electric.services.CategoryService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Libraries;


@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(Category category){
        try{
            category.setId(Libraries.generateId(Constants.lengthId));
            category.setStatus(true);
            category.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            category.setModifiedAt(new Timestamp(System.currentTimeMillis()));

            return this.categoryRepository.save(category);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Category getById(String id){
        try{
            Optional<Category> category = this.categoryRepository.findById(id);
            if(category.isPresent() == false){
                return null;
            }

            return category.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Category edit(Category category){
        try{
            category.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.categoryRepository.save(category);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Category> getAll(){
        try{
            List<Category> categories = this.categoryRepository.findAll();
            return categories;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean checkListCategories(List<String> categories){
        try{
            boolean result = true;
            for (String category : categories) {
                if (this.getById(category) == null){
                    result = false;
                }
            }

            return result;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Category> paginate(int num, int page){
        try{
            Optional<List<Category>> lst = this.categoryRepository.paginate(num, (page-1)*num);
            if (lst.isPresent() == false){
                return null;
            }

            return lst.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
