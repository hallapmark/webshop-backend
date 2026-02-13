package ee.markh.webshopbackend.controller;

import ee.markh.webshopbackend.dto.ProductRequest;
import ee.markh.webshopbackend.dto.ProductResponse;
import ee.markh.webshopbackend.entity.Product;
import ee.markh.webshopbackend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // localhost:8080/products --> käivitan selle funktsiooni
    // localhost:8080/products?categoryId=electronics&page=0&size=20&sort=id,asc
    // size ja sort on optional automaatselt
    // permit all
    @GetMapping("products")
    public Page<ProductResponse> getProducts(@RequestParam Long categoryId, Pageable pageable) {
        return productService.getProducts(categoryId, pageable);
    }

    // localhost:8080/products/id
    // permit all
    @GetMapping("products/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    // admin use - return ALL products (no pagination, no filtering)
    @GetMapping("admin/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // tegusõna tavaliselt ei panda siia endpoint nime sisse. annotation on tegusõna
    // req admin
    @PostMapping("products")
    public List<Product> createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    // req admin
    @PostMapping("many-products")
    public List<Product> addManyProducts(@RequestBody List<ProductRequest> requests) {
        return productService.createManyProducts(requests);
    }

    // localhost:8080/products/id
    // req admin
    @DeleteMapping("products/{id}")
    public List<Product> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    // req admin
    @PutMapping("products")
    public List<Product> editProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

}
