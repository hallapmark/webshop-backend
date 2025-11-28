package ee.markh.webshopbackend.repository;

import ee.markh.webshopbackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product, String> {

}
