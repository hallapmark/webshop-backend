package ee.markh.webshopbackend.service;

import ee.markh.webshopbackend.dto.ProductRequest;
import ee.markh.webshopbackend.dto.ProductResponse;
import ee.markh.webshopbackend.entity.Category;
import ee.markh.webshopbackend.entity.Product;
import ee.markh.webshopbackend.repository.CategoryRepository;
import ee.markh.webshopbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    public Page<ProductResponse> getProducts(Long categoryId, Pageable pageable) {
        Page<Product> products;
        if (categoryId == 0) {
            products = productRepository.findAll(pageable);
        } else {
            products = productRepository.findByCategoryId(categoryId, pageable);
        }
        return products.map(this::toProductResponse);
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        return toProductResponse(product);
    }

    /* Get all products (admin use, no pagination). */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> createProduct(ProductRequest productRequest) {
        if (productRequest.getPrice() <= 0) {
            throw new RuntimeException("Price must be greater than 0");
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setSlug(productRequest.getSlug());
        product.setDescription_en(productRequest.getDescription_en());
        product.setDescription_et(productRequest.getDescription_et());
        product.setPrice(productRequest.getPrice());
        product.setCategory(category);

        productRepository.save(product);
        return productRepository.findAll();
    }

    public List<Product> createManyProducts(List<Product> products) {
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

    public List<Product> deleteProduct(Long id) {
        productRepository.deleteById(id);
        return productRepository.findAll();
    }

    public List<Product> updateProduct(Product product) {
        if (product.getId() == null) {
            throw new RuntimeException("Cannot edit product without id");
        }
        productRepository.save(product);
        return productRepository.findAll();
    }

    public ProductResponse toProductResponse(Product product) {
        // TODO: Use MapStruct in the future
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description_en(product.getDescription_en())
                .description_et(product.getDescription_et())
                .price(product.getPrice())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .imageUrl(imageService.getImageUrl(product.getSlug()))
                .build();
    }
}
