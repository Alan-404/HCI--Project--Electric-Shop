package com.hci.electric.dtos.category;

import com.hci.electric.models.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HandleCategoryResponse {
    private Boolean success;
    private String message;
    private Category category;
}
