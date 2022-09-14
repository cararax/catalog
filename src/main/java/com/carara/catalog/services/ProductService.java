package com.carara.catalog.services;

import com.carara.catalog.dto.CategoryDTO;
import com.carara.catalog.dto.ProductDTO;
import com.carara.catalog.entities.Category;
import com.carara.catalog.entities.Product;
import com.carara.catalog.exceptions.DatabaseException;
import com.carara.catalog.exceptions.ResourceNotFoundException;
import com.carara.catalog.repositories.CategoryRepository;
import com.carara.catalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> productList = productRepository.findAll(pageable);
        return productList.map(ProductDTO::new);
    }

    @Transactional
    public ProductDTO findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product entity = productOptional.orElseThrow(() -> new ResourceNotFoundException("Entity not found for id: " + id + "."));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product();
        copyDtoToEntity(productDTO, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product entity = productRepository.getOne(id);
            copyDtoToEntity(productDTO, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("Product with id " + id + " not found.");
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Product with id " + id + " not found.");
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseException("Integrity Violation.");
        }
    }

    private void copyDtoToEntity(ProductDTO productDTO, Product entity) {
        entity.setName(productDTO.getName());
        entity.setDescription(productDTO.getDescription());
        entity.setDate(productDTO.getDate());
        entity.setImageURL(productDTO.getImageURL());
        entity.setPrice(productDTO.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO categoryDto : productDTO.getCategories()) {
            Category categoryEntity = categoryRepository.getOne(categoryDto.getId());
            entity.getCategories().add(categoryEntity);
        }
    }
}