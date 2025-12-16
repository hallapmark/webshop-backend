package ee.markh.webshopbackend.controller;

import ee.markh.webshopbackend.entity.Product;
import ee.markh.webshopbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // annab võimekuse API päringuid siia faili teha
public class ProductController {
    // base URL - localhost:8080
    // API endpoint - products

    @Autowired
    private ProductRepository productRepository;

    // localhost:8080/products --> käivitan selle funktsiooni
    // localhost:8080/products?categoryId=electronics&page=0&size=20&sort=id,asc
    // size ja sort on optional automaatselt
    // permit all
    @GetMapping("products")
    public Page<Product> getProducts(@RequestParam Long categoryId, Pageable pageable) {
        if (categoryId == 0) {
            return productRepository.findAll(pageable);
        } else {
            return productRepository.findByCategoryId(categoryId, pageable);
        }
    }

    // admin use - return ALL products (no pagination, no filtering)
    @GetMapping("admin/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // tegusõna tavaliselt ei panda siia endpoint nime sisse. annotation on tegusõna
    // req admin
    @PostMapping("products")
    public List<Product> addProduct(@RequestBody Product product) {
        if (product.getId() != null) {
            throw new RuntimeException("Cannot add product with id");
        }
        if (product.getPrice() <= 0) {
            throw new RuntimeException("Price must be greater than 0");
        }
        productRepository.save(product);
        return productRepository.findAll();
    }

    // req admin
    @PostMapping("many-products")
    public List<Product> addManyProducts(@RequestBody List<Product> products) {
        for (Product product : products) {
            if (product.getId() != null) {
                throw new RuntimeException("Cannot add product with id");
            }
            if (product.getPrice() <= 0) {
                throw new RuntimeException("Price must be greater than 0");
            }
        }
        productRepository.saveAll(products);
        return productRepository.findAll();
    }

    // localhost:8080/products/id
    // req admin
    @DeleteMapping("products/{id}")
    public List<Product> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return productRepository.findAll();
    }

    // localhost:8080/products/uuid-uuid
    // permit all
    @GetMapping("products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }
    // RequestParam ja PathVariable --> mõlemaid võib üldjuhul kasutada
    // siiski kindlasti Requestparami vaja järgnevatel juhtudel:
    // 1. kui on valikuline (nullable) (RequestParam(required = false))
    // 2. kui on mitu parameetrit. Pathvariable puhul läheks segaseks mis mille jaoks on. eelistatud requestparam siis

    // req admin
    @PutMapping("products")
    public List<Product> editProduct(@RequestBody Product product) {
        if (product.getId() == null) {
            throw new RuntimeException("Cannot edit product without id");
        }
        productRepository.save(product);
        return productRepository.findAll();
    }

}
