package com.javaspringboot.DevicesManagementSystemBackend.controller;

import com.javaspringboot.DevicesManagementSystemBackend.advice.HttpResponse;
import com.javaspringboot.DevicesManagementSystemBackend.dto.CategoryDTO;
import com.javaspringboot.DevicesManagementSystemBackend.exception.ExceptionHandling;
import com.javaspringboot.DevicesManagementSystemBackend.model.Category;
import com.javaspringboot.DevicesManagementSystemBackend.payload.response.MessageResponse;
import com.javaspringboot.DevicesManagementSystemBackend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class CategoryController extends ExceptionHandling {
    @Autowired
    private CategoryRepository categoryRepository;
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDTO addCategoryRequest) {
        if(categoryRepository.existsByDescription(addCategoryRequest.getDescription())){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Category is already taken!"));
        }
        categoryRepository.save(new Category(addCategoryRequest.getName(),addCategoryRequest.getDescription()));
        return ResponseEntity.ok(new MessageResponse("Add succesfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) throws EmptyResultDataAccessException {
        try{
            categoryRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Delete succesfully"));
        } catch (Exception e){
            throw new EmptyResultDataAccessException("Delete failure",1);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findCategoryById(@RequestParam Long id) throws NoSuchElementException{
        Optional<Category> category = categoryRepository.findById(id);
        return new ResponseEntity(category.get(), OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return new ResponseEntity<>(categories, OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryDTO categoryDTO) throws NoSuchElementException {
        Optional<Category> category = categoryRepository.findById(id);
        if(!category.isPresent()){
            throw new NoSuchElementException("No category found with id:" +id);
        } else {
            Category _category = category.get();
            _category.setName(categoryDTO.getName());
            _category.setDescription(categoryDTO.getDescription());
            try {
                categoryRepository.save(_category);
                return ResponseEntity.ok(new MessageResponse("Update succesfully"));
            }catch (Exception e){
                return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                        "","Category is already taken!"));
            }
        }
    }
}
