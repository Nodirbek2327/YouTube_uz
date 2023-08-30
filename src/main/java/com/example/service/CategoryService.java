package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.dto.TagDTO;
import com.example.entity.CategoryEntity;
import com.example.entity.TagEntity;
import com.example.exp.ItemNotFoundException;
import com.example.repository.CategoryRepository;
import com.example.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public CategoryDTO update(Integer id, CategoryDTO categoryDTO) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) throw new ItemNotFoundException("Category not found");
        CategoryEntity entity = optional.get();
        entity.setName(categoryDTO.getName());
        categoryRepository.save(entity);
        categoryDTO.setId(entity.getId());
        return categoryDTO;
    }

    public String delete(Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) throw new ItemNotFoundException("Category not found");
        categoryRepository.deleteById(id);
        return "Deleted!!!";
    }


    public List<CategoryDTO> getList() {
        List<CategoryDTO> list = new LinkedList<>();
        Iterable<CategoryEntity> all = categoryRepository.findAll();
        all.forEach(tagEntity -> {
            list.add(toDTO(tagEntity));
        });
        return list;
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public CategoryDTO getCategory(Integer categoryId) {
        Optional<CategoryEntity> optional = categoryRepository.findById(categoryId);
        if (optional.isEmpty()) return null;
        CategoryEntity entity= optional.get();
        CategoryDTO dto=new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

}
