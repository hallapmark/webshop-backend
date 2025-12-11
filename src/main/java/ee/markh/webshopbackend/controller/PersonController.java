package ee.markh.webshopbackend.controller;

import ee.markh.webshopbackend.entity.Person;
import ee.markh.webshopbackend.entity.PersonRole;
import ee.markh.webshopbackend.model.AuthToken;
import ee.markh.webshopbackend.model.LoginCredentials;
import ee.markh.webshopbackend.repository.PersonRepository;
import ee.markh.webshopbackend.service.JwtService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

// we have OpenAPI 3.0 (Swagger) documentation (see pom.xml) so we can do
// http://localhost:8080/swagger-ui.html
// or http://localhost:8080/v3/api-docs for the json

@RestController
@Tag(name = "Persons", description = "Person management APIs")
public class PersonController {
    // base URL - localhost:8080
    // API endpoint - persons

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JwtService jwtService;

    // testing swagger docs (not looking to add the tags comprehensively for now)
    @Operation(summary = "Get all persons")
    @GetMapping("persons")
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    @PostMapping("persons")
    public Person addPerson(@RequestBody Person person) {
        if (person.getId() != null) {
            throw new RuntimeException("Cannot add person with id");
        }
        // this endpoint only allows Customers to be added. Make sure the frontend user is
        // not increasing their appropriate privilege:
        person.setRole(PersonRole.CUSTOMER);
        return personRepository.save(person);
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

    // TODO: Remove or make editor/admin exclusive
    @PutMapping("persons")
    public Person editPerson(@RequestBody Person person) {
        if (person.getId() == null) {
            throw new RuntimeException("Cannot edit person without id");
        }
        Long personId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        System.out.println("personId:" + personId);
        System.out.println("Person objektist:" + person.getId());
        if (!person.getId().equals(personId)) {
            throw new RuntimeException("You can only edit your own profile");
        }
        Person dbPerson = personRepository.findById(personId).orElseThrow();
        // Whitelist the fields we want to allow to be changed.
        // Email, password, role, id not changeable from frontend for now
        dbPerson.setFirstName(person.getFirstName());
        dbPerson.setLastName(person.getLastName());
        return personRepository.save(dbPerson);
    }

    // TODO: Use AuthenticationPrincipal later. For now implementing in a more manual manner for practice
//    @PutMapping("editownprofile")
//    public Person editOwnProfile(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestBody Person person) {
//        // Expects "Bearer <token>"
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new RuntimeException("Invalid Authorization header");
//        }
//
//        // https://swagger.io/docs/specification/v3_0/authentication/bearer-authentication/
//        String token = authHeader.substring(7);
//
//        String idFromToken;
//        try {
//            idFromToken = Jwts
//                .parser()
//                .verifyWith(getSecretKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getSubject();
//        } catch (JwtException e) {
//            throw new RuntimeException("Invalid token");
//        }
//        Long idFromTokenLong = Long.valueOf(idFromToken);
//
//        Person existing = personRepository.findById(idFromTokenLong)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Whitelist the fields we want to allow to be changed.
//        // Email, password, role, id not changeable from frontend for now
//        existing.setFirstName(person.getFirstName());
//        existing.setLastName(person.getLastName());
//        personRepository.save(existing);
//        return existing;
//    }

    @PostMapping("login")
    public AuthToken login(@RequestBody LoginCredentials loginCredentials) {
        Person person = personRepository.findByEmail(loginCredentials.getEmail());
        if (person == null) {
            throw new RuntimeException("Invalid email");
        }
        if (!person.getPassword().equals(loginCredentials.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(person);
    }

    //localhost:8080/person?token=
    @GetMapping("person")
    public Person getPersonByToken() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return personRepository.findById(id).orElseThrow();
    }
}
