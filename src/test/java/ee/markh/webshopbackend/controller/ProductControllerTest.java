package ee.markh.webshopbackend.controller;

import ee.markh.webshopbackend.dto.ProductRequest;
import ee.markh.webshopbackend.dto.ProductResponse;
import ee.markh.webshopbackend.entity.Category;
import ee.markh.webshopbackend.entity.Product;
import ee.markh.webshopbackend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/* Unit tests for {@link ProductController} using Mockito to mock {@link ProductService}.
Pure unit tests. */
// TODO: Ilmselt peaks controllerile lisama ka @WebMvcTest integration testid. Eraldi klassi?
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Category category;
    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setSlug("test-product");
        product.setDescription_en("English description");
        product.setDescription_et("Estonian description");
        product.setPrice(99.99);
        product.setCategory(category);

        productRequest = new ProductRequest();
        productRequest.setName("New Product");
        productRequest.setSlug("new-product");
        productRequest.setDescription_en("New English description");
        productRequest.setDescription_et("New Estonian description");
        productRequest.setPrice(49.99);
        productRequest.setCategoryId(1L);

        productResponse = ProductResponse.builder()
                .id(1L)
                .name("Test Product")
                .slug("test-product")
                .description_en("English description")
                .description_et("Estonian description")
                .price(99.99)
                .categoryId(1L)
                .categoryName("Electronics")
                .imageUrl("https://cdn.example.com/images/test-product.webp")
                .build();
    }

    @Test
    void getProducts_returnsPage() {
        // given
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductResponse> expectedPage = new PageImpl<>(List.of(productResponse), pageable, 1);

        BDDMockito.given(productService.getProducts(categoryId, pageable)).willReturn(expectedPage);

        // when
        Page<ProductResponse> result = productController.getProducts(categoryId, pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(productResponse, result.getContent().get(0));
        BDDMockito.then(productService).should().getProducts(categoryId, pageable);
    }

    @Test
    void getAllProducts_returnsList() {
        // given
        List<Product> expectedProducts = List.of(product);
        BDDMockito.given(productService.getAllProducts()).willReturn(expectedProducts);

        // when
        List<Product> result = productController.getAllProducts();

        // then
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
        BDDMockito.then(productService).should().getAllProducts();
    }

    @Test
    void createProduct_returnsList() {
        // given
        List<Product> expectedProducts = List.of(product);
        BDDMockito.given(productService.createProduct(productRequest)).willReturn(expectedProducts);

        // when
        List<Product> result = productController.createProduct(productRequest);

        // then
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
        BDDMockito.then(productService).should().createProduct(productRequest);
    }

    @Test
    void addManyProducts_returnsList() {
        // given
        ProductRequest req2 = new ProductRequest();
        req2.setName("Another Product");
        req2.setPrice(10.0);
        req2.setCategoryId(1L);

        List<ProductRequest> requests = List.of(productRequest, req2);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Another Product");
        product2.setPrice(10.0);
        product2.setCategory(category);

        List<Product> expectedProducts = List.of(product, product2);
        BDDMockito.given(productService.createManyProducts(requests))
                .willReturn(expectedProducts);

        // when
        List<Product> result = productController.addManyProducts(requests);

        // then
        assertEquals(expectedProducts, result);
        BDDMockito.then(productService).should().createManyProducts(requests);
    }
}
