package org.sbb.sbb.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.domain.category.entity.Category;
import org.sbb.sbb.domain.category.repository.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategory(String name) {
        return categoryRepository.findByCategory(name).orElseThrow(() -> new NoSuchElementException("카테고리를 찾을 수 없습니다."));
    }

    public Category setCategory(String name) {
        if (categoryRepository.findByCategory(name).isPresent()) {
            throw new DataIntegrityViolationException("이미 해당 카테고리가 존재합니다.");
        }

        return categoryRepository.save(Category.builder().category(name).build());
    }

}
