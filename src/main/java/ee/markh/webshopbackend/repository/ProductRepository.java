package ee.markh.webshopbackend.repository;

import ee.markh.webshopbackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Long> {
    boolean existsBySlug(String slug);
    Page<Product> findByCategoryId(Long id, Pageable pageable);
}
// 20 on default lk peal