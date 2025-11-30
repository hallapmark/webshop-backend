package ee.markh.webshopbackend.controller;

import ee.markh.webshopbackend.entity.Product;
import ee.markh.webshopbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // annab võimekuse API päringuid siia faili teha
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    // base URL - localhost:8080
    // API endpoint - products

    @Autowired
    private ProductRepository productRepository;

    // localhost:8080/products --> käivitan selle funktsiooni
    // localhost:8080/products?categoryId=electronics&page=0&size=20&sort=id,asc
    // size ja sort on optional automaatselt
    @GetMapping("products")
    public Page<Product> getProducts(@RequestParam Long categoryId, Pageable pageable) {
        if (categoryId == 0) {
            return productRepository.findAll(pageable);
        } else {
            return productRepository.findByCategoryId(categoryId, pageable);
        }
    }

    // tegusõna tavaliselt ei panda siia endpoint nime sisse. annotation on tegusõna
    @PostMapping("products")
    public List<Product> addProduct(@RequestBody Product product, Pageable pageable) {
        if (product.getId() != null) {
            throw new RuntimeException("Cannot add product with id");
        }
        if (product.getPrice() <= 0) {
            throw new RuntimeException("Price must be greater than 0");
        }
        productRepository.save(product);
        return productRepository.findAll();
    }

    @PostMapping("many-products")
    public List<Product> addManyProducts(@RequestBody List<Product> products) {
        productRepository.saveAll(products);
        return productRepository.findAll();
    }

    // requestparamiga
    // localhost:8080/products?id=
    @DeleteMapping("products")
    public List<Product> deleteProduct(@RequestParam Long id) {
        productRepository.deleteById(id);
        return productRepository.findAll();
    }

    // localhost:8080/products/uuid-uuid
    @GetMapping("products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }
    // RequestParam ja PathVariable --> mõlemaid võib üldjuhul kasutada
    // siiski kindlasti Requestparami vaja järgnevatel juhtudel:
    // 1. kui on valikuline (nullable) (RequestParam(required = false))
    // 2. kui on mitu parameetrit. Pathvariable puhul läheks segaseks mis mille jaoks on. eelistatud requestparam siis

    @PutMapping("products")
    public List<Product> editProduct(@RequestBody Product product) {
        if (product.getId() == null) {
            throw new RuntimeException("Cannot edit product without id");
        }
        productRepository.save(product);
        return productRepository.findAll();
    }

}
