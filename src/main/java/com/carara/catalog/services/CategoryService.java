package com.carara.catalog.services;

import com.carara.catalog.dto.CategoryDTO;
import com.carara.catalog.entities.Category;
import com.carara.catalog.exceptions.EntityNotFoundException;
import com.carara.catalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> categoryList = categoryRepository.findAll();
//        List<CategoryDTO> categoryDTOList = categoryList.stream().map(CategoryDTO::new).collect(Collectors.toList());
//        List<CategoryDTO> categoryDTOList = categoryList.stream().map(entity -> new CategoryDTO(entity)).collect(Collectors.toList());
//        List<CategoryDTO> categoryDTOList = new ArrayList<>();
//        for(Category entity : categoryList){
//            categoryDTOList.add(new CategoryDTO(entity));
//        }
        return categoryList.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    public CategoryDTO findById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        Category category = categoryOptional.orElseThrow(() -> new EntityNotFoundException("Entity not found for id: " + id + "."));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category entity = new Category();
        entity.setName(categoryDTO.getName());
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }
}
