package ee.markh.webshopbackend.controller;

import ee.markh.webshopbackend.entity.Category;
import ee.markh.webshopbackend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
// for local dev, enable CrossOrigin (where frontend is deployed)
//@CrossOrigin(origins = "http://localhost:5173") // nyyd SecurityConfigis
public class CategoryController {
    // base URL - localhost:8080
    // API endpoint - categories

//    @Autowired
//    Random random;

    @Autowired
    private CategoryRepository categoryRepository;

    // localhost:8080/categories --> käivitan selle funktsiooni
    // permit all
    @GetMapping("categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll(Sort.by("id"));
    }

    // tegusõna tavaliselt ei panda siia endpoint nime sisse. annotation on tegusõna
    // req admin
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
    // req admin
    @DeleteMapping("categories/{id}")
    public List<Category> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return categoryRepository.findAll();
    }

    // req admin
    @PutMapping("categories")
    public List<Category> editCategory(@RequestBody Category category) {
        if (category.getId() == null) {
            throw new RuntimeException("Cannot edit category without id");
        }

        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);

        return categoryRepository.findAll();
    }

    // bean test
//    @GetMapping("random-number")
//    public int randomNumber() {
////        Random random = new Random();
//        System.out.println(random);
//        return random.nextInt(100);
//    }
}