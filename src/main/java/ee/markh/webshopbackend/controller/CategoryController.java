package ee.markh.webshopbackend.controller;

import ee.markh.webshopbackend.entity.Category;
import ee.markh.webshopbackend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {
    // base URL - localhost:8080
    // API endpoint - categories

    @Autowired
    private CategoryRepository categoryRepository;

    // localhost:8080/categories --> käivitan selle funktsiooni
    @GetMapping("categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    // tegusõna tavaliselt ei panda siia endpoint nime sisse. annotation on tegusõna
    @PostMapping("categories")
    public List<Category> addCategory(@RequestBody Category category) {
        if (category.getId() != null) {
            throw new RuntimeException("Cannot add category with id");
        }
        categoryRepository.save(category);
        return categoryRepository.findAll();
    }

    // requestparamiga
    // localhost:8080/categories?id=
    @DeleteMapping("categories")
    public List<Category> deleteCategory(@RequestParam Long id) {
        categoryRepository.deleteById(id);
        return categoryRepository.findAll();
    }
}