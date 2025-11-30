package ee.markh.webshopbackend.repository;

import ee.markh.webshopbackend.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
