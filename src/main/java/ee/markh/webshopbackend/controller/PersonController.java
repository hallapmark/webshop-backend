package ee.markh.webshopbackend.controller;

import ee.markh.webshopbackend.entity.Person;
import ee.markh.webshopbackend.model.AuthToken;
import ee.markh.webshopbackend.model.LoginCredentials;
import ee.markh.webshopbackend.repository.PersonRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

// we have OpenAPI 3.0 (Swagger) documentation (see pom.xml) so we can do
// http://localhost:8080/swagger-ui.html
// or http://localhost:8080/v3/api-docs for the json

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Persons", description = "Person management APIs")
public class PersonController {
    // base URL - localhost:8080
    // API endpoint - persons

    @Autowired
    private PersonRepository personRepository;

    // access env variable JWT_SIGNING_KEY_BASE64. More Spring magic/auto-conversion
    @Value("${jwt.signing.key.base64}")
    private String jwtSigningKeyBase64;
    // base 64 formaadis peab jwtSigningKeyBase64 olema

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSigningKeyBase64));
    }

    private Date getExpiration() {
        return new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3));
    }

    // test swagger docs (not looking to add the tags comprehensively for now)
    @Operation(summary = "Get all persons")
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

    @PostMapping("login")
    public AuthToken login(@RequestBody LoginCredentials loginCredentials) {
        Person person = personRepository.findByEmail(loginCredentials.getEmail());
        if (person == null) {
            throw new RuntimeException("Invalid email");
        }
        if (!person.getPassword().equals(loginCredentials.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        AuthToken authToken = new AuthToken();
        authToken.setToken(Jwts
                .builder()
                .subject(person.getId().toString())
                .expiration(getExpiration())
                .signWith(getSecretKey())
                .compact());
        return authToken;
    }

    //localhost:8080/person?token=
    @GetMapping("person")
    public Person getPersonByToken(@RequestParam String token) {
        String id = Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return personRepository.findById(Long.parseLong(id)).orElseThrow();
    }
}
