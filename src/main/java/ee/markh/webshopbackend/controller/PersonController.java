package ee.markh.webshopbackend.controller;

import ee.markh.webshopbackend.entity.Person;
import ee.markh.webshopbackend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PersonController {
    // base URL - localhost:8080
    // API endpoint - persons

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("persons")
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    @PostMapping("persons")
    public List<Person> addPerson(@RequestBody Person person) {
        if (person.getId() != null) {
            throw new RuntimeException("Cannot add person with id");
        }
        personRepository.save(person);
        return personRepository.findAll();
    }

    @PostMapping("many-persons")
    public List<Person> addManyPersons(@RequestBody List<Person> persons) {
        personRepository.saveAll(persons);
        return personRepository.findAll();
    }

    // localhost:8080/persons?id=
    @DeleteMapping("persons")
    public List<Person> deletePerson(@RequestParam Long id) {
        personRepository.deleteById(id);
        return personRepository.findAll();
    }

    // localhost:8080/persons/uuid-uuid
    @GetMapping("persons/{id}")
    public Person getPerson(@PathVariable Long id) {
        return personRepository.findById(id).orElse(null);
    }

    @PutMapping("persons")
    public List<Person> editPerson(@RequestBody Person person) {
        if (person.getId() == null) {
            throw new RuntimeException("Cannot edit person without id");
        }
        personRepository.save(person);
        return personRepository.findAll();
    }

}
