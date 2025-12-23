package ee.markh.webshopbackend.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.markh.webshopbackend.entity.Category;
import ee.markh.webshopbackend.entity.Product;
import ee.markh.webshopbackend.repository.CategoryRepository;
import ee.markh.webshopbackend.repository.ProductRepository;
import ee.markh.webshopbackend.seed.dto.ProductSeed;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Profile("seed")
@Transactional
public class ProductSeeder {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    public ProductSeeder(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ObjectMapper objectMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }

    public void seed() throws IOException {
        InputStream is = new ClassPathResource("seed/products.json").getInputStream();
        List<ProductSeed> seeds = objectMapper.readValue(is, new TypeReference<List<ProductSeed>>() {});

        for (ProductSeed seed : seeds) {
            if (productRepository.existsBySlug(seed.slug())) continue;

            Category category = categoryRepository
                    .findById(seed.category().id())
                    .orElseThrow(() -> new IllegalStateException("Missing category id " + seed.category().id()));

            Product product = new Product(
                    null,
                    seed.name(),
                    seed.slug(),
                    seed.description_en(),
                    seed.description_et(),
                    seed.price(),
                    category
            );

            productRepository.save(product);
        }
    }
}
