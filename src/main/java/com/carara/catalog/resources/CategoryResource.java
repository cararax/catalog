package com.carara.catalog.resources;

import com.carara.catalog.dto.CategoryDTO;
import com.carara.catalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryResource {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categoryList = categoryService.findAll();
        return ResponseEntity.ok().body(categoryList);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.findById(id);
        return ResponseEntity.ok().body(categoryDTO);
    }
}
