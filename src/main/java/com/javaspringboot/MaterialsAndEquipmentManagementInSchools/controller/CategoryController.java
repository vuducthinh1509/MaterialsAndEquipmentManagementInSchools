package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.controller;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.HttpResponse;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.dto.CategoryDTO;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.exception.ExceptionHandling;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.exception.domain.CategoryNotFoundException;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.model.Category;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.payload.response.MessageResponse;
import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

;

@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping("/api/category")
public class CategoryController extends ExceptionHandling {
    @Autowired
    private CategoryRepository categoryRepository;
    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody CategoryDTO addCategoryRequest) {
//        if(categoryRepository.existsByDescription(addCategoryRequest.getDescription())&& categoryRepository.existsByName(addCategoryRequest.getName())){
//            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
//                    "","Category is already taken!"));
//        }
        Category category = categoryRepository.findCategoryByNameAndDescription(addCategoryRequest.getName(), addCategoryRequest.getDescription());
        if(category!=null){
            return ResponseEntity.badRequest().body(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                    "","Category is already taken!"));
        }
        categoryRepository.save(new Category(addCategoryRequest.getName(),addCategoryRequest.getDescription()));
        return new ResponseEntity(new MessageResponse("Add succesfully"),HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Delete succesfully"));
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> findById(@RequestParam Long id){
        Optional<Category> category = categoryRepository.findById(id);
        return new ResponseEntity(category.get(), OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return new ResponseEntity<>(categories, OK);
    }

    @GetMapping("/list-distinct-categories")
    public ResponseEntity<List<?>> getDistinctCategories() {
        List<?> categories = categoryRepository.getDistinctByName();
        return new ResponseEntity<>(categories, OK);
    }

    @GetMapping("/list-by-category-name")
    public ResponseEntity<List<String>> listByCategoryName(@RequestParam String name){
        List<String> descriptionStr = categoryRepository.listDescriptionByCategoryName(name);
        return new ResponseEntity<>(descriptionStr, OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateById(@RequestParam Long id,@Valid @RequestBody CategoryDTO categoryDTO) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if(!category.isPresent()){
            throw new CategoryNotFoundException(id.toString());
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
