package com.hci.electric.dtos.category;

import java.util.List;

import com.hci.electric.models.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginateCategory {
    private List<Category> categories;
    private int totalPages;
}
