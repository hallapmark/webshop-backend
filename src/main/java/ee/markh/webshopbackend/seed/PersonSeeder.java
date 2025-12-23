package ee.markh.webshopbackend.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.markh.webshopbackend.entity.Person;
import ee.markh.webshopbackend.entity.PersonRole;
import ee.markh.webshopbackend.repository.PersonRepository;
import ee.markh.webshopbackend.seed.dto.PersonSeed;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Profile("seed")
public class PersonSeeder {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public PersonSeeder(PersonRepository personRepository,
                        PasswordEncoder passwordEncoder,
                        ObjectMapper objectMapper) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void seed() throws IOException {
        // skip if any rows exist
        if (personRepository.count() > 0) return;

        InputStream is = new ClassPathResource("seed/persons.json").getInputStream();
        List<PersonSeed> seeds = objectMapper.readValue(is, new TypeReference<List<PersonSeed>>() {});

        for (PersonSeed seed : seeds) {
            // skip if email already exists
            if (personRepository.findByEmail(seed.email()) != null) continue;

            Person person = new Person();
            person.setFirstName(seed.firstName());
            person.setLastName(seed.lastName());
            person.setEmail(seed.email());
            person.setRole(PersonRole.valueOf(seed.role()));
            person.setPassword(passwordEncoder.encode(seed.password()));

            personRepository.save(person);
        }
    }
}
