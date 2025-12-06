package ee.markh.webshopbackend.repository;

import ee.markh.webshopbackend.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByEmail(String email);
}
