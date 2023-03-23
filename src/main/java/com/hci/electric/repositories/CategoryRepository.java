package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Category;
import com.hci.electric.utils.queries.CategoryQuery;

public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query(value = CategoryQuery.paginate, nativeQuery = true)
    public Optional<List<Category>> paginate(int limit, int offset);
}
