package ee.markh.webshopbackend.repository;

import ee.markh.webshopbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category, Long> {

}
